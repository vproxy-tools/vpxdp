package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class UMemInfo extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        io.vproxy.vpxdp.XskRingProd.LAYOUT.withName("fillRing"),
        io.vproxy.vpxdp.XskRingCons.LAYOUT.withName("compRing"),
        ValueLayout.ADDRESS.withName("umem"),
        ValueLayout.JAVA_INT.withName("isShared"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */,
        ValueLayout.ADDRESS.withName("buffer"),
        ValueLayout.JAVA_INT.withName("bufferSize"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */,
        ValueLayout.ADDRESS.withName("chunks"),
        ValueLayout.JAVA_INT.withName("txMetadataLen"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private final io.vproxy.vpxdp.XskRingProd fillRing;

    public io.vproxy.vpxdp.XskRingProd getFillRing() {
        return this.fillRing;
    }

    private final io.vproxy.vpxdp.XskRingCons compRing;

    public io.vproxy.vpxdp.XskRingCons getCompRing() {
        return this.compRing;
    }

    private static final VarHandleW umemVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("umem")
        )
    );

    public io.vproxy.vpxdp.UMem getUmem() {
        var SEG = umemVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.UMem(SEG);
    }

    public void setUmem(io.vproxy.vpxdp.UMem umem) {
        if (umem == null) {
            umemVH.set(MEMORY, MemorySegment.NULL);
        } else {
            umemVH.set(MEMORY, umem.MEMORY);
        }
    }

    private static final VarHandleW isSharedVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("isShared")
        )
    );

    public int getIsShared() {
        return isSharedVH.getInt(MEMORY);
    }

    public void setIsShared(int isShared) {
        isSharedVH.set(MEMORY, isShared);
    }

    private static final VarHandleW bufferVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("buffer")
        )
    );

    public MemorySegment getBuffer() {
        var SEG = bufferVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setBuffer(MemorySegment buffer) {
        if (buffer == null) {
            bufferVH.set(MEMORY, MemorySegment.NULL);
        } else {
            bufferVH.set(MEMORY, buffer);
        }
    }

    private static final VarHandleW bufferSizeVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("bufferSize")
        )
    );

    public int getBufferSize() {
        return bufferSizeVH.getInt(MEMORY);
    }

    public void setBufferSize(int bufferSize) {
        bufferSizeVH.set(MEMORY, bufferSize);
    }

    private static final VarHandleW chunksVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("chunks")
        )
    );

    public io.vproxy.vpxdp.ChunkArray getChunks() {
        var SEG = chunksVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.ChunkArray(SEG);
    }

    public void setChunks(io.vproxy.vpxdp.ChunkArray chunks) {
        if (chunks == null) {
            chunksVH.set(MEMORY, MemorySegment.NULL);
        } else {
            chunksVH.set(MEMORY, chunks.MEMORY);
        }
    }

    private static final VarHandleW txMetadataLenVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("txMetadataLen")
        )
    );

    public int getTxMetadataLen() {
        return txMetadataLenVH.getInt(MEMORY);
    }

    public void setTxMetadataLen(int txMetadataLen) {
        txMetadataLenVH.set(MEMORY, txMetadataLen);
    }

    public UMemInfo(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        this.fillRing = new io.vproxy.vpxdp.XskRingProd(MEMORY.asSlice(OFFSET, io.vproxy.vpxdp.XskRingProd.LAYOUT.byteSize()));
        OFFSET += io.vproxy.vpxdp.XskRingProd.LAYOUT.byteSize();
        this.compRing = new io.vproxy.vpxdp.XskRingCons(MEMORY.asSlice(OFFSET, io.vproxy.vpxdp.XskRingCons.LAYOUT.byteSize()));
        OFFSET += io.vproxy.vpxdp.XskRingCons.LAYOUT.byteSize();
        OFFSET += 8;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
        OFFSET += 8;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
    }

    public UMemInfo(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    private static final MethodHandle shareMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.UMemInfo.LAYOUT.getClass(), "vp_umem_share", MemorySegment.class /* self */);

    public io.vproxy.vpxdp.UMemInfo share() {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) shareMH.invokeExact(MEMORY);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.UMemInfo(RESULT);
    }

    private static final MethodHandle closeMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_umem_close", MemorySegment.class /* self */, boolean.class /* cleanBuffer */);

    public void close(boolean cleanBuffer) {
        try {
            closeMH.invokeExact(MEMORY, cleanBuffer);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
    }

    private static final MethodHandle fillRingFillupMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_xdp_fill_ring_fillup", MemorySegment.class /* self */);

    public void fillRingFillup() {
        try {
            fillRingFillupMH.invokeExact(MEMORY);
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
        SB.append("UMemInfo{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("fillRing => ");
            PanamaUtils.nativeObjectToString(getFillRing(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("compRing => ");
            PanamaUtils.nativeObjectToString(getCompRing(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("umem => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getUmem(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("isShared => ");
            SB.append(getIsShared());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("buffer => ");
            SB.append(PanamaUtils.memorySegmentToString(getBuffer()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("bufferSize => ");
            SB.append(getBufferSize());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("chunks => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getChunks(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("txMetadataLen => ");
            SB.append(getTxMetadataLen());
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<UMemInfo> {
        public Array(MemorySegment buf) {
            super(buf, UMemInfo.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, UMemInfo.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, UMemInfo.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.UMemInfo ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "UMemInfo.Array";
        }

        @Override
        protected UMemInfo construct(MemorySegment seg) {
            return new UMemInfo(seg);
        }

        @Override
        protected MemorySegment getSegment(UMemInfo value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<UMemInfo> {
        private Func(io.vproxy.pni.CallSite<UMemInfo> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<UMemInfo> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<UMemInfo> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<UMemInfo> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "UMemInfo.Func";
        }

        @Override
        protected UMemInfo construct(MemorySegment seg) {
            return new UMemInfo(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:9f982354565ff5a81bf4f834a6ee99b0b07190b3d01690f2b5540b68925c3336
