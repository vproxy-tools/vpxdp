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

    @Name("vp_mac2port_add_into_map")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int addMac2Port(MemorySegment mac, String ifname);

    @Name("vp_port2dev_add_into_map")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int addPort2Dev(String ifname);

    @Name("bpf_map__lookup_elem")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int lookup(MemorySegment key, long keySize, MemorySegment value, long valueSize, long flags);

    @Name("bpf_map__update_elem")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int update(MemorySegment key, long keySize, MemorySegment value, long valueSize, long flags);

    @Name("bpf_map__delete_elem")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int delete(MemorySegment key, long keySize, long flags);

    @Name("bpf_map__pin")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int pin(String path);

    @Name("bpf_map__unpin")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int unpin(String path);

    @Name("bpf_map__pin_path")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract String getPinPath();

    @Name("bpf_map__get_next_key")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int getNextKey(MemorySegment cur, MemorySegment nx, long keySize);
}
