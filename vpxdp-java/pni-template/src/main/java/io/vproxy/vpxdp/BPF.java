package io.vproxy.vpxdp;

import io.vproxy.pni.annotation.*;

import java.lang.foreign.MemorySegment;

@Struct
@PointerOnly
abstract class PNIBPFObject {
    @Name("vp_bpfobj_find_map_by_name")
    @Style(Styles.critical)
    @LinkerOption.Critical
    @NoAlloc
    abstract PNIBPFMap findMapByName(String name);

    @Name("vp_bpfobj_release")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract void release();
}

@Struct
@PointerOnly
abstract class PNIBPFMap {
    @Name("vp_xsk_add_into_map")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int addXsk(int key, PNIXskInfo xsk);

    @Name("vp_mac_add_into_map")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int addMac(MemorySegment mac, String ifname);

    @Name("vp_mac_remove_from_map")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int removeMac(MemorySegment mac);
}
