package io.vproxy.vpxdp;

import io.vproxy.pni.annotation.*;

import java.lang.foreign.MemorySegment;

@Struct
class PNIChecksumOut {
    MemorySegment upPos;
    MemorySegment upCsumPos;
}

@Downcall
interface PNICheckSum {
    @Name("vp_pkt_ether_csum")
    @Style(Styles.critical)
    @LinkerOption.Critical
    int ether(MemorySegment raw, int len, int flags);

    @Name("vp_pkt_ether_csum_ex")
    @Style(Styles.critical)
    @LinkerOption.Critical
    int etherEx(MemorySegment raw, int len, int flags, PNIChecksumOut out);
}
