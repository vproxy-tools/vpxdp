package io.vproxy.vpxdp;

import io.vproxy.pni.annotation.*;

import java.lang.foreign.MemorySegment;

@Struct
class PNIXskRing {
    @Unsigned int cachedProd;
    @Unsigned int cachedCons;
    @Unsigned int mask;
    @Unsigned int size;
    @NativeType("uint32_t*") MemorySegment producer;
    @NativeType("uint32_t*") MemorySegment consumer;
    MemorySegment ring;
    @NativeType("uint32_t*") MemorySegment flags;
}

@Struct
class PNIXskRingProd extends PNIXskRing {
}

@Struct
class PNIXskRingCons extends PNIXskRing {
}

@Struct
@PointerOnly
class PNIUMem {
}

@Struct
@PointerOnly
abstract class PNIXskSocket {
    @Name("xsk_socket__fd")
    @Style(Styles.critical)
    @LinkerOption.Critical
    abstract int fd();
}
