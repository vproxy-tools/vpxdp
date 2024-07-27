package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class ChecksumOut extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.ADDRESS.withName("upPos"),
        ValueLayout.ADDRESS.withName("upCsumPos")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW upPosVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("upPos")
        )
    );

    public MemorySegment getUpPos() {
        var SEG = upPosVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setUpPos(MemorySegment upPos) {
        if (upPos == null) {
            upPosVH.set(MEMORY, MemorySegment.NULL);
        } else {
            upPosVH.set(MEMORY, upPos);
        }
    }

    private static final VarHandleW upCsumPosVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("upCsumPos")
        )
    );

    public MemorySegment getUpCsumPos() {
        var SEG = upCsumPosVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setUpCsumPos(MemorySegment upCsumPos) {
        if (upCsumPos == null) {
            upCsumPosVH.set(MEMORY, MemorySegment.NULL);
        } else {
            upCsumPosVH.set(MEMORY, upCsumPos);
        }
    }

    public ChecksumOut(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
    }

    public ChecksumOut(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("ChecksumOut{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("upPos => ");
            SB.append(PanamaUtils.memorySegmentToString(getUpPos()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("upCsumPos => ");
            SB.append(PanamaUtils.memorySegmentToString(getUpCsumPos()));
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<ChecksumOut> {
        public Array(MemorySegment buf) {
            super(buf, ChecksumOut.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, ChecksumOut.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, ChecksumOut.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.ChecksumOut ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "ChecksumOut.Array";
        }

        @Override
        protected ChecksumOut construct(MemorySegment seg) {
            return new ChecksumOut(seg);
        }

        @Override
        protected MemorySegment getSegment(ChecksumOut value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<ChecksumOut> {
        private Func(io.vproxy.pni.CallSite<ChecksumOut> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<ChecksumOut> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<ChecksumOut> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<ChecksumOut> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "ChecksumOut.Func";
        }

        @Override
        protected ChecksumOut construct(MemorySegment seg) {
            return new ChecksumOut(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:f5bd2241f0f32bab499b2d9509730d635a6cccf9a22108be7c616cc107b4c607
