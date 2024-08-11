package io.vproxy.vpxdp;

import io.vproxy.base.util.bytearray.MemorySegmentByteArray;
import io.vproxy.pni.Allocator;
import io.vproxy.pni.PNIString;
import io.vproxy.pni.array.IntArray;
import io.vproxy.pni.array.PointerArray;
import sun.misc.Signal;

import java.lang.foreign.MemorySegment;

import static io.vproxy.vpxdp.XDPConsts.*;

public class SampleUser {
    private static final int XDP_MODE = XDP_FLAGS_SKB_MODE;
    private static final int HEADROOM = 512;
    private static final int META_LEN = 32;
    private static final String TEXT_GREEN = "\033[0;32m";
    private static final String TEXT_RED = "\033[0;31m";
    private static final String TEXT_NORMAL = "\033[0m";

    private static boolean sigint = false;

    public static void main(String[] args) throws Exception {
        System.loadLibrary("elf");
        System.loadLibrary("xdp");
        System.loadLibrary("vpxdp");

        if (args.length < 1) {
            throw new IllegalArgumentException("the first argument should be ifname to attach the ebpf program");
        }
        var ifname = args[0];

        var allocator = Allocator.ofConfined();

        var bpfobj = XDP.get().attachBPFObjectToIf(
            new PNIString(allocator, "sample_kern.o"),
            new PNIString(allocator, "xdp_sock"),
            new PNIString(allocator, ifname),
            XDP_MODE);
        if (bpfobj == null) {
            throw new Exception("attach bpf obj to " + ifname + " failed");
        }
        var map = bpfobj.findMapByName(new PNIString(allocator, "xsks_map"));
        if (map == null) {
            throw new Exception("unable to find map `xsk_map` in bpf");
        }

        var umem = XDP.get().createUMem(64, 32, 32, 4096, HEADROOM, META_LEN);
        if (umem == null) {
            throw new Exception("failed to create umem");
        }
        var xsk = XDP.get().createXsk(new PNIString(allocator, ifname), 0, umem, 32, 32,
            XDP_MODE, XDP_COPY, 0, 0);
        if (xsk == null) {
            throw new Exception("failed to create xsk");
        }
        var ret = map.addXsk(0, xsk);
        if (ret != 0) {
            throw new Exception("failed to add xsk into bpf map");
        }

        bpfobj.release();
        allocator.close();

        allocator = Allocator.ofConfined();

        System.out.println("ready to poll");

        var fds = new PNIPollFD.Array(allocator, 2);
        fds.get(0).setFd(xsk.getXsk().fd());
        fds.get(0).setEvents(POLLIN);

        Signal.handle(new Signal("INT"), _ -> sigint = true);
        Signal.handle(new Signal("TERM"), _ -> sigint = true);

        System.out.println("press ctrl-c to exit");
        int cnt = 0;
        var idxRx = new IntArray(allocator, 1);
        var chunkPtr = new PointerArray(allocator, 1);
        while (true) {
            if (sigint) break;
            ret = Poll.get().poll(fds, 1, 50);
            if (ret <= 0 || ret > 1) {
                if (sigint) break;
                continue;
            }

            System.out.println("poll triggered");

            idxRx.set(0, -1);
            int rcvd = xsk.fetchPacket(idxRx, chunkPtr);
            if (rcvd == 0) {
                continue;
            }
            System.out.println("rcvd = " + rcvd);
            for (int i = 0; i < rcvd; ++i) {
                xsk.fetchPacket(idxRx, chunkPtr);
                var chunk = new ChunkInfo(chunkPtr.get(0));
                System.out.println("received packet:" +
                                   " cnt=" + (++cnt) +
                                   " addr=" + chunk.getAddr() +
                                   " pkt=" + (chunk.getPkt().address() - umem.getBuffer().address()) +
                                   " len=" + chunk.getPktLen() +
                                   " umem_size=" + umem.getChunks().getSize() +
                                   " umem_used=" + umem.getChunks().getUsed());
                hexdump("received", chunk.getPkt(), chunk.getPktLen(), 16);

                var pkt = new MemorySegmentByteArray(
                    chunk.getPkt().reinterpret(14 + 40 /* xdp-tutorial uses ipv6 only */ + 1 /* icmp type */));
                if (chunk.getPktLen() >= pkt.length()
                    && (((pkt.get(12) & 0xff) << 8) | (pkt.get(13) & 0xff)) == 0x86dd
                    && (pkt.get(14 + 6) & 0xff) == 58) {
                    // swap mac dst and src
                    for (int j = 0; j < 6; ++j) {
                        byte b = pkt.get(j);
                        pkt.set(j, pkt.get(6 + j));
                        pkt.set(6 + j, b);
                    }
                    // swap ipv6 src and dst
                    for (int j = 0; j < 16; ++j) {
                        byte b = pkt.get(14 + 8 + j);
                        pkt.set(14 + 8 + j, pkt.get(14 + 8 + 16 + j));
                        pkt.set(14 + 8 + 16 + j, b);
                    }
                    // set icmp type
                    pkt.set(14 + 40, (byte) 129);
                    ChunkInfo sChunk;
                    boolean isCopy = (cnt % 2) == 1;
                    boolean isOffload = (cnt % 4) >= 2;
                    if (!isCopy) {
                        chunk.setRef((byte) (chunk.getRef() + 1));
                        sChunk = chunk;
                    } else {
                        var chunk2 = umem.getChunks().fetch();
                        if (chunk2 == null) {
                            System.out.println("ERR! umem no enough chunks:" +
                                               " size=" + umem.getChunks().getSize() +
                                               " used=" + umem.getChunks().getUsed());
                            continue;
                        }
                        chunk2.setPktAddr(chunk2.getAddr() + META_LEN);
                        chunk2.setPkt(MemorySegment.ofAddress(chunk2.getRealAddr().address() + META_LEN));
                        int pktLen = chunk.getPktLen();
                        chunk2.setPktLen(pktLen);
                        chunk2.getPkt().reinterpret(pktLen).copyFrom(chunk.getPkt().reinterpret(pktLen));

                        sChunk = chunk2;
                    }
                    if (!isOffload) {
                        sChunk.setCsumFlags(VP_CSUM_ALL);
                    } else {
                        sChunk.setCsumFlags(VP_CSUM_IP | VP_CSUM_UP_PSEUDO | VP_CSUM_XDP_OFFLOAD);
                    }
                    System.out.println("no_copy = " + (isCopy ? TEXT_RED : TEXT_GREEN) + (isCopy ? "off" : "on") + TEXT_NORMAL + " , " +
                                       "csum_offload = " + (isOffload ? TEXT_GREEN : TEXT_RED) + (isOffload ? "on" : "off") + TEXT_NORMAL);
                    xsk.writePacket(sChunk);
                }

                umem.getChunks().releaseChunk(chunk);
            }
            xsk.rxRelease(rcvd);
            umem.fillRingFillup();
            xsk.completeTx();
        }
        xsk.close();
        umem.close(true);
        XDP.get().detachBPFObjectFromIf(new PNIString(allocator, ifname));

        System.out.println("received " + cnt + " packets, exit");
    }

    private static void hexdump(String desc, MemorySegment pkt, int len, int perLine) {
        var hex = new MemorySegmentByteArray(pkt.reinterpret(len)).toHexString();
        System.out.println(desc + ":");
        while (true) {
            if (hex.length() <= perLine * 2) {
                System.out.println(hex);
                break;
            }
            var s = hex.substring(0, perLine * 2);
            hex = hex.substring(perLine * 2);
            System.out.println(s);
        }
    }
}
