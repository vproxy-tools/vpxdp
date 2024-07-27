package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class ChunkInfo extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.ADDRESS.withName("umem"),
        ValueLayout.ADDRESS.withName("xsk"),
        ValueLayout.JAVA_BYTE.withName("ref"),
        ValueLayout.JAVA_BYTE.withName("txRef"),
        MemoryLayout.sequenceLayout(6L, ValueLayout.JAVA_BYTE) /* padding */,
        ValueLayout.JAVA_LONG.withName("addr"),
        ValueLayout.JAVA_LONG.withName("endAddr"),
        ValueLayout.ADDRESS.withName("realAddr"),
        ValueLayout.JAVA_LONG.withName("pktAddr"),
        ValueLayout.ADDRESS.withName("pkt"),
        ValueLayout.JAVA_INT.withName("pktLen"),
        ValueLayout.JAVA_INT.withName("csumFlags")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW umemVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("umem")
        )
    );

    public io.vproxy.vpxdp.UMemInfo getUmem() {
        var SEG = umemVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.UMemInfo(SEG);
    }

    public void setUmem(io.vproxy.vpxdp.UMemInfo umem) {
        if (umem == null) {
            umemVH.set(MEMORY, MemorySegment.NULL);
        } else {
            umemVH.set(MEMORY, umem.MEMORY);
        }
    }

    private static final VarHandleW xskVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("xsk")
        )
    );

    public io.vproxy.vpxdp.XskInfo getXsk() {
        var SEG = xskVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.XskInfo(SEG);
    }

    public void setXsk(io.vproxy.vpxdp.XskInfo xsk) {
        if (xsk == null) {
            xskVH.set(MEMORY, MemorySegment.NULL);
        } else {
            xskVH.set(MEMORY, xsk.MEMORY);
        }
    }

    private static final VarHandleW refVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("ref")
        )
    );

    public byte getRef() {
        return refVH.getByte(MEMORY);
    }

    public void setRef(byte ref) {
        refVH.set(MEMORY, ref);
    }

    private static final VarHandleW txRefVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("txRef")
        )
    );

    public byte getTxRef() {
        return txRefVH.getByte(MEMORY);
    }

    public void setTxRef(byte txRef) {
        txRefVH.set(MEMORY, txRef);
    }

    private static final VarHandleW addrVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("addr")
        )
    );

    public long getAddr() {
        return addrVH.getLong(MEMORY);
    }

    public void setAddr(long addr) {
        addrVH.set(MEMORY, addr);
    }

    private static final VarHandleW endAddrVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("endAddr")
        )
    );

    public long getEndAddr() {
        return endAddrVH.getLong(MEMORY);
    }

    public void setEndAddr(long endAddr) {
        endAddrVH.set(MEMORY, endAddr);
    }

    private static final VarHandleW realAddrVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("realAddr")
        )
    );

    public MemorySegment getRealAddr() {
        var SEG = realAddrVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setRealAddr(MemorySegment realAddr) {
        if (realAddr == null) {
            realAddrVH.set(MEMORY, MemorySegment.NULL);
        } else {
            realAddrVH.set(MEMORY, realAddr);
        }
    }

    private static final VarHandleW pktAddrVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("pktAddr")
        )
    );

    public long getPktAddr() {
        return pktAddrVH.getLong(MEMORY);
    }

    public void setPktAddr(long pktAddr) {
        pktAddrVH.set(MEMORY, pktAddr);
    }

    private static final VarHandleW pktVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("pkt")
        )
    );

    public MemorySegment getPkt() {
        var SEG = pktVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setPkt(MemorySegment pkt) {
        if (pkt == null) {
            pktVH.set(MEMORY, MemorySegment.NULL);
        } else {
            pktVH.set(MEMORY, pkt);
        }
    }

    private static final VarHandleW pktLenVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("pktLen")
        )
    );

    public int getPktLen() {
        return pktLenVH.getInt(MEMORY);
    }

    public void setPktLen(int pktLen) {
        pktLenVH.set(MEMORY, pktLen);
    }

    private static final VarHandleW csumFlagsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("csumFlags")
        )
    );

    public int getCsumFlags() {
        return csumFlagsVH.getInt(MEMORY);
    }

    public void setCsumFlags(int csumFlags) {
        csumFlagsVH.set(MEMORY, csumFlags);
    }

    public ChunkInfo(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += 8;
        OFFSET += 8;
        OFFSET += ValueLayout.JAVA_BYTE.byteSize();
        OFFSET += ValueLayout.JAVA_BYTE.byteSize();
        OFFSET += 6; /* padding */
        OFFSET += ValueLayout.JAVA_LONG_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_LONG_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_LONG_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
    }

    public ChunkInfo(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("ChunkInfo{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("umem => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getUmem(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("xsk => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getXsk(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("ref => ");
            SB.append(getRef());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("txRef => ");
            SB.append(getTxRef());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("addr => ");
            SB.append(getAddr());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("endAddr => ");
            SB.append(getEndAddr());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("realAddr => ");
            SB.append(PanamaUtils.memorySegmentToString(getRealAddr()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("pktAddr => ");
            SB.append(getPktAddr());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("pkt => ");
            SB.append(PanamaUtils.memorySegmentToString(getPkt()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("pktLen => ");
            SB.append(getPktLen());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("csumFlags => ");
            SB.append(getCsumFlags());
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<ChunkInfo> {
        public Array(MemorySegment buf) {
            super(buf, ChunkInfo.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, ChunkInfo.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, ChunkInfo.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.ChunkInfo ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "ChunkInfo.Array";
        }

        @Override
        protected ChunkInfo construct(MemorySegment seg) {
            return new ChunkInfo(seg);
        }

        @Override
        protected MemorySegment getSegment(ChunkInfo value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<ChunkInfo> {
        private Func(io.vproxy.pni.CallSite<ChunkInfo> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<ChunkInfo> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<ChunkInfo> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<ChunkInfo> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "ChunkInfo.Func";
        }

        @Override
        protected ChunkInfo construct(MemorySegment seg) {
            return new ChunkInfo(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:e7b8d26302b96e2cae3734dd0d009201e890d18557e3f76bfa013ae9caf455fb
