package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class BPFMapReuseUnion extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.unionLayout(
        ValueLayout.ADDRESS.withName("map"),
        ValueLayout.ADDRESS.withName("pinpath")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW mapVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("map")
        )
    );

    public io.vproxy.vpxdp.BPFMap getMap() {
        var SEG = mapVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.BPFMap(SEG);
    }

    public void setMap(io.vproxy.vpxdp.BPFMap map) {
        if (map == null) {
            mapVH.set(MEMORY, MemorySegment.NULL);
        } else {
            mapVH.set(MEMORY, map.MEMORY);
        }
    }

    private static final VarHandleW pinpathVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("pinpath")
        )
    );

    public PNIString getPinpath() {
        var SEG = pinpathVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new PNIString(SEG);
    }

    public void setPinpath(String pinpath, Allocator ALLOCATOR) {
        this.setPinpath(new PNIString(ALLOCATOR, pinpath));
    }

    public void setPinpath(PNIString pinpath) {
        if (pinpath == null) {
            pinpathVH.set(MEMORY, MemorySegment.NULL);
        } else {
            pinpathVH.set(MEMORY, pinpath.MEMORY);
        }
    }

    public BPFMapReuseUnion(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += 8;
        OFFSET = 0;
        OFFSET += 8;
        OFFSET = 0;
    }

    public BPFMapReuseUnion(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        CORRUPTED_MEMORY = true;
        SB.append("BPFMapReuseUnion(\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("map => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getMap(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("pinpath => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getPinpath(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append(")@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<BPFMapReuseUnion> {
        public Array(MemorySegment buf) {
            super(buf, BPFMapReuseUnion.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, BPFMapReuseUnion.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, BPFMapReuseUnion.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.BPFMapReuseUnion ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFMapReuseUnion.Array";
        }

        @Override
        protected BPFMapReuseUnion construct(MemorySegment seg) {
            return new BPFMapReuseUnion(seg);
        }

        @Override
        protected MemorySegment getSegment(BPFMapReuseUnion value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<BPFMapReuseUnion> {
        private Func(io.vproxy.pni.CallSite<BPFMapReuseUnion> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<BPFMapReuseUnion> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFMapReuseUnion> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFMapReuseUnion> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFMapReuseUnion.Func";
        }

        @Override
        protected BPFMapReuseUnion construct(MemorySegment seg) {
            return new BPFMapReuseUnion(seg);
        }
    }
}
// metadata.generator-version: pni 0.0.20
// sha256:b69a1df69f06cb36395e971b0e56cb1d3d85912690d4a54a075841308a2c5eb0
