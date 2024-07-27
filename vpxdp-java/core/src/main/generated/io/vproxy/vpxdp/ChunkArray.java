package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class ChunkArray extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("frameCount"),
        ValueLayout.JAVA_INT.withName("size"),
        ValueLayout.JAVA_INT.withName("used"),
        ValueLayout.JAVA_INT.withName("idx"),
        ValueLayout.ADDRESS.withName("array")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW frameCountVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("frameCount")
        )
    );

    public int getFrameCount() {
        return frameCountVH.getInt(MEMORY);
    }

    public void setFrameCount(int frameCount) {
        frameCountVH.set(MEMORY, frameCount);
    }

    private static final VarHandleW sizeVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("size")
        )
    );

    public int getSize() {
        return sizeVH.getInt(MEMORY);
    }

    public void setSize(int size) {
        sizeVH.set(MEMORY, size);
    }

    private static final VarHandleW usedVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("used")
        )
    );

    public int getUsed() {
        return usedVH.getInt(MEMORY);
    }

    public void setUsed(int used) {
        usedVH.set(MEMORY, used);
    }

    private static final VarHandleW idxVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("idx")
        )
    );

    public int getIdx() {
        return idxVH.getInt(MEMORY);
    }

    public void setIdx(int idx) {
        idxVH.set(MEMORY, idx);
    }

    private static final VarHandleW arrayVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("array")
        )
    );

    public io.vproxy.vpxdp.ChunkInfo getArray() {
        var SEG = arrayVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.ChunkInfo(SEG);
    }

    public void setArray(io.vproxy.vpxdp.ChunkInfo array) {
        if (array == null) {
            arrayVH.set(MEMORY, MemorySegment.NULL);
        } else {
            arrayVH.set(MEMORY, array.MEMORY);
        }
    }

    public ChunkArray(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 8;
    }

    public ChunkArray(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    private static final MethodHandle seekMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.ChunkInfo.LAYOUT.getClass(), "vp_chunk_seek", MemorySegment.class /* self */, long.class /* addrOff */);

    public io.vproxy.vpxdp.ChunkInfo seek(long addrOff) {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) seekMH.invokeExact(MEMORY, addrOff);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.ChunkInfo(RESULT);
    }

    private static final MethodHandle fetchMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.ChunkInfo.LAYOUT.getClass(), "vp_chunk_fetch", MemorySegment.class /* self */);

    public io.vproxy.vpxdp.ChunkInfo fetch() {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) fetchMH.invokeExact(MEMORY);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.ChunkInfo(RESULT);
    }

    private static final MethodHandle releaseChunkMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_chunk_release", MemorySegment.class /* self */, io.vproxy.vpxdp.ChunkInfo.LAYOUT.getClass() /* chunk */);

    public void releaseChunk(io.vproxy.vpxdp.ChunkInfo chunk) {
        try {
            releaseChunkMH.invokeExact(MEMORY, (MemorySegment) (chunk == null ? MemorySegment.NULL : chunk.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("ChunkArray{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("frameCount => ");
            SB.append(getFrameCount());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("size => ");
            SB.append(getSize());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("used => ");
            SB.append(getUsed());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("idx => ");
            SB.append(getIdx());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("array => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getArray(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<ChunkArray> {
        public Array(MemorySegment buf) {
            super(buf, ChunkArray.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, ChunkArray.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, ChunkArray.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.ChunkArray ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "ChunkArray.Array";
        }

        @Override
        protected ChunkArray construct(MemorySegment seg) {
            return new ChunkArray(seg);
        }

        @Override
        protected MemorySegment getSegment(ChunkArray value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<ChunkArray> {
        private Func(io.vproxy.pni.CallSite<ChunkArray> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<ChunkArray> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<ChunkArray> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<ChunkArray> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "ChunkArray.Func";
        }

        @Override
        protected ChunkArray construct(MemorySegment seg) {
            return new ChunkArray(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:c3e64d33144a57041d1f3c527563ea32f72d7121736a0c9a45e7a695c91876a0
