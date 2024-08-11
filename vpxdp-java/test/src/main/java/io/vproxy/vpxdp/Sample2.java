package io.vproxy.vpxdp;

import io.vproxy.base.util.LogType;
import io.vproxy.base.util.Logger;
import io.vproxy.pni.Allocator;
import io.vproxy.pni.PNIString;
import sun.misc.Signal;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

import static io.vproxy.vpxdp.XDPConsts.*;

public class Sample2 {
    private static final String BPF_OBJECT_FILE = "./prebuilt/default.o";
    private static final String PIN_PATH = "/sys/fs/bpf/sample2/srcmac2count_map";
    private static final int XDP_MODE = XDP_FLAGS_SKB_MODE;
    private static volatile boolean stop = false;

    public static void main(String[] args) throws Exception {
        System.loadLibrary("elf");
        System.loadLibrary("xdp");
        System.loadLibrary("vpxdp");

        var allocator = Allocator.ofConfined();

        var bpfobj = XDP.get().attachBPFObjectToIf(
            new PNIString(allocator, BPF_OBJECT_FILE),
            new PNIString(allocator, "xdp_sock"),
            new PNIString(allocator, "v0o"),
            XDP_MODE);

        if (bpfobj == null) {
            throw new Exception("failed to attach default.o to v0o");
        }

        var mac2portMap = bpfobj.findMapByName(new PNIString(allocator, "mac2port_map"));
        if (mac2portMap == null) {
            throw new Exception("failed to find mac2port_map");
        }
        var mac = allocator.allocate(6);
        mac.copyFrom(MemorySegment.ofArray(new byte[]{0, 0, 0, 0, 0, 1}));
        mac2portMap.addMac2Port(mac, new PNIString(allocator, "v0o"));
        mac.copyFrom(MemorySegment.ofArray(new byte[]{0, 0, 0, 0, 0, 2}));
        mac2portMap.addMac2Port(mac, new PNIString(allocator, "v1o"));

        var srcmac2countMap = bpfobj.findMapByName(new PNIString(allocator, "srcmac2count_map"));
        if (srcmac2countMap == null) {
            throw new Exception("failed to find srcmac2count_map");
        }

        var err = srcmac2countMap.pin(new PNIString(allocator, PIN_PATH));
        if (err != 0) {
            throw new Exception("failed to pin srcmac2count_map");
        }

        var reuseMaps = new BPFMapReuse.Array(allocator, 3);
        {
            reuseMaps.get(0).setName(new PNIString(allocator, "mac2port_map"));
            reuseMaps.get(0).setType(VP_BPF_MAP_REUSE_TYPE_MAP);
            reuseMaps.get(0).getUnion().setMap(mac2portMap);
        }
        {
            reuseMaps.get(1).setName(new PNIString(allocator, "srcmac2count_map"));
            reuseMaps.get(1).setType(VP_BPF_MAP_REUSE_TYPE_PIN_PATH);
            reuseMaps.get(1).getUnion().setPinpath(PIN_PATH, allocator);
        }
        reuseMaps.get(2).setName(null);

        var bpfobj2 = XDP.get().attachBPFObjectToIfReuseMap(
            new PNIString(allocator, BPF_OBJECT_FILE),
            new PNIString(allocator, "xdp_sock"),
            new PNIString(allocator, "v1o"),
            XDP_MODE,
            reuseMaps
        );
        if (bpfobj2 == null) {
            throw new Exception("failed to attach default.o to v1o");
        }

        bpfobj.release();
        bpfobj2.release();

        reuseMaps.get(1).setName(null);
        {
            reuseMaps.get(0).setName(new PNIString(allocator, "srcmac2count_map"));
            reuseMaps.get(0).setType(VP_BPF_MAP_REUSE_TYPE_PIN_PATH);
            reuseMaps.get(0).getUnion().setPinpath(PIN_PATH, allocator);
        }
        bpfobj = XDP.get().loadBPFObject(new PNIString(allocator, BPF_OBJECT_FILE), reuseMaps);
        if (bpfobj == null) {
            throw new Exception("failed to load default.o");
        }

        srcmac2countMap = bpfobj.findMapByName(new PNIString(allocator, "srcmac2count_map"));
        if (srcmac2countMap == null) {
            throw new Exception("failed to find srcmac2count_map (2)");
        }

        Signal.handle(new Signal("INT"), _ -> stop = true);
        Signal.handle(new Signal("TERM"), _ -> stop = true);

        Logger.alert("environment is properly prepared, begin to fetch data from bpf map ...");

        var key = allocator.allocate(6);
        var value = allocator.allocate(4);
        while (true) {
            if (stop) {
                break;
            }
            //noinspection BusyWait
            Thread.sleep(500);
            if (stop) {
                break;
            }

            err = srcmac2countMap.getNextKey(null, key, 6);
            if (err < 0) {
                if (err != -ENOENT) {
                    Logger.warn(LogType.ALERT, "getNextKey return err = " + err);
                }
                continue;
            }
            while (true) {
                err = srcmac2countMap.lookup(key, 6, value, 4, 0);
                if (err < 0) {
                    Logger.error(LogType.ALERT, "failed to find statistics for " + mem2mac(key));
                    continue;
                }
                Logger.alert(mem2mac(key) + " => " + mem2int(value));

                err = srcmac2countMap.getNextKey(key, key, 6);
                if (err < 0) {
                    if (err != -ENOENT) {
                    Logger.warn(LogType.ALERT, "getNextKey return err = " + err);
                    }
                    break;
                }
            }
        }

        Logger.alert("cleanup ...");

        srcmac2countMap.unpin(new PNIString(allocator, PIN_PATH));
        bpfobj.release();
        XDP.get().detachBPFObjectFromIf(new PNIString(allocator, "v0o"));
        XDP.get().detachBPFObjectFromIf(new PNIString(allocator, "v1o"));
    }

    private static String mem2mac(MemorySegment mem) {
        var sb = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            int b = mem.get(ValueLayout.JAVA_BYTE, i) & 0xff;
            if (i != 0) {
                sb.append(":");
            }
            if (b < 16) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    private static int mem2int(MemorySegment mem) {
        return mem.get(ValueLayout.JAVA_INT, 0);
    }
}
