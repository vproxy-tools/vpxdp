package io.vproxy.vpxdp;

import io.vproxy.pni.annotation.*;

import java.lang.foreign.MemorySegment;

@Struct
abstract class PNIXskInfo {
    PNIXskRingCons rx;
    PNIXskRingProd tx;
    @Pointer PNIUMemInfo umem;
    @Pointer PNIXskSocket xsk;
    int ifIndex;
    int rxRingSize;
    int txRingSize;
    int outstandingTx;
    int flags;

    @Name("vp_xsk_close")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void close();

    @Name("vp_xdp_fetch_pkt")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int fetchPacket(@Raw @Unsigned int[] idxRxPtr, @Raw MemorySegment[] chunkPtrs);

    @Name("vp_xdp_rx_release")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void rxRelease(int cnt);

    @Name("vp_xdp_write_pkt")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int writePacket(PNIChunkInfo chunk);

    @Name("vp_xdp_write_pkts")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int writePackets(int size, @Raw MemorySegment[] chunkPtrs);

    @Name("vp_xdp_complete_tx")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void completeTx();
}

@Struct
abstract class PNIUMemInfo {
    PNIXskRingProd fillRing;
    PNIXskRingCons compRing;
    @Pointer PNIUMem umem;
    int isShared;
    MemorySegment buffer;
    int bufferSize;
    @Pointer PNIChunkArray chunks;
    int txMetadataLen;

    @Name("vp_umem_share")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    abstract PNIUMemInfo share();

    @Name("vp_umem_close")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void close(boolean cleanBuffer);

    @Name("vp_xdp_fill_ring_fillup")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void fillRingFillup();
}

@Struct
class PNIChunkInfo {
    @Pointer PNIUMemInfo umem;
    @Pointer PNIXskInfo xsk;
    @Unsigned byte ref;
    @Unsigned byte txRef;
    @Unsigned long addr;
    @Unsigned long endAddr;
    MemorySegment realAddr;
    @Unsigned long pktAddr;
    MemorySegment pkt;
    @Unsigned int pktLen;
    int csumFlags;
}

@Struct
abstract class PNIChunkArray {
    int frameCount;
    int size;
    int used;
    int idx; // for fetching chunks
    @Pointer PNIChunkInfo array;

    @Name("vp_chunk_seek")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    abstract PNIChunkInfo seek(@Unsigned long addrOff);

    @Name("vp_chunk_fetch")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    abstract PNIChunkInfo fetch();

    @Name("vp_chunk_release")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void releaseChunk(PNIChunkInfo chunk);
}

@Downcall
interface PNIXDP {
    @Name("vp_bpfobj_attach_to_if")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    PNIBPFObject attachBPFObjectToIf(String filepath, String prog, String ifname, int attachFlags);

    @Name("vp_bpfobj_detach_from_if")
    @Style(Styles.critical)
    @LinkerOption.Critical
    int detachBPFObjectFromIf(String ifname);

    @Name("vp_umem_create")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    PNIUMemInfo createUMem(int chunkSize, int fillRingSize, int compRingSize, int frameCount, int headroom, int metaLen);

    @Name("vp_xsk_create")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    PNIXskInfo createXsk(String ifname, int queueId, PNIUMemInfo umem,
                         int rxRingSize, int txRingSize, int xdpFlags, int bindFlags,
                         int busyPollBudget, int vpFlags);
}
