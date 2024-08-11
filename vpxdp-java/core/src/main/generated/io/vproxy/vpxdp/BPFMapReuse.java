package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class BPFMapReuse extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.ADDRESS.withName("name"),
        ValueLayout.JAVA_INT.withName("type"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */,
        io.vproxy.vpxdp.BPFMapReuseUnion.LAYOUT.withName("union")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW nameVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("name")
        )
    );

    public PNIString getName() {
        var SEG = nameVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new PNIString(SEG);
    }

    public void setName(String name, Allocator ALLOCATOR) {
        this.setName(new PNIString(ALLOCATOR, name));
    }

    public void setName(PNIString name) {
        if (name == null) {
            nameVH.set(MEMORY, MemorySegment.NULL);
        } else {
            nameVH.set(MEMORY, name.MEMORY);
        }
    }

    private static final VarHandleW typeVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("type")
        )
    );

    public int getType() {
        return typeVH.getInt(MEMORY);
    }

    public void setType(int type) {
        typeVH.set(MEMORY, type);
    }

    private final io.vproxy.vpxdp.BPFMapReuseUnion union;

    public io.vproxy.vpxdp.BPFMapReuseUnion getUnion() {
        return this.union;
    }

    public BPFMapReuse(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += 8;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
        this.union = new io.vproxy.vpxdp.BPFMapReuseUnion(MEMORY.asSlice(OFFSET, io.vproxy.vpxdp.BPFMapReuseUnion.LAYOUT.byteSize()));
        OFFSET += io.vproxy.vpxdp.BPFMapReuseUnion.LAYOUT.byteSize();
    }

    public BPFMapReuse(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("BPFMapReuse{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("name => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getName(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("type => ");
            SB.append(getType());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("union => ");
            PanamaUtils.nativeObjectToString(getUnion(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<BPFMapReuse> {
        public Array(MemorySegment buf) {
            super(buf, BPFMapReuse.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, BPFMapReuse.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, BPFMapReuse.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.BPFMapReuse ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFMapReuse.Array";
        }

        @Override
        protected BPFMapReuse construct(MemorySegment seg) {
            return new BPFMapReuse(seg);
        }

        @Override
        protected MemorySegment getSegment(BPFMapReuse value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<BPFMapReuse> {
        private Func(io.vproxy.pni.CallSite<BPFMapReuse> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<BPFMapReuse> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFMapReuse> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFMapReuse> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFMapReuse.Func";
        }

        @Override
        protected BPFMapReuse construct(MemorySegment seg) {
            return new BPFMapReuse(seg);
        }
    }
}
// metadata.generator-version: pni 0.0.20
// sha256:924c9c81978a151227a7d982376ec34a656eaecc635a38c442148ed0a47f7086
