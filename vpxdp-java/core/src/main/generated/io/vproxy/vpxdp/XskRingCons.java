package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class XskRingCons extends io.vproxy.vpxdp.XskRing implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        io.vproxy.vpxdp.XskRing.LAYOUT

    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    public XskRingCons(MemorySegment MEMORY) {
        super(MEMORY);
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += io.vproxy.vpxdp.XskRing.LAYOUT.byteSize();
    }

    public XskRingCons(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("XskRingCons{\n");
        SB.append(" ".repeat(INDENT + 4)).append("SUPER => ");
        {
            INDENT += 4;
            SB.append("XskRing{\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("cachedProd => ");
                SB.append(getCachedProd());
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("cachedCons => ");
                SB.append(getCachedCons());
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("mask => ");
                SB.append(getMask());
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("size => ");
                SB.append(getSize());
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("producer => ");
                SB.append(PanamaUtils.memorySegmentToString(getProducer()));
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("consumer => ");
                SB.append(PanamaUtils.memorySegmentToString(getConsumer()));
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("ring => ");
                SB.append(PanamaUtils.memorySegmentToString(getRing()));
            }
            SB.append(",\n");
            {
                SB.append(" ".repeat(INDENT + 4)).append("flags => ");
                SB.append(PanamaUtils.memorySegmentToString(getFlags()));
            }
            SB.append("\n");
            SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
            INDENT -= 4;
            SB.append("\n");

        }
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<XskRingCons> {
        public Array(MemorySegment buf) {
            super(buf, XskRingCons.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, XskRingCons.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, XskRingCons.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.XskRingCons ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskRingCons.Array";
        }

        @Override
        protected XskRingCons construct(MemorySegment seg) {
            return new XskRingCons(seg);
        }

        @Override
        protected MemorySegment getSegment(XskRingCons value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<XskRingCons> {
        private Func(io.vproxy.pni.CallSite<XskRingCons> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<XskRingCons> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<XskRingCons> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<XskRingCons> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskRingCons.Func";
        }

        @Override
        protected XskRingCons construct(MemorySegment seg) {
            return new XskRingCons(seg);
        }
    }
}
// metadata.generator-version: pni 0.0.20
// sha256:b5512d6ee9a24574c214e9185c2894d94ac48d192ccb8b0c1f728a5dc636098d
