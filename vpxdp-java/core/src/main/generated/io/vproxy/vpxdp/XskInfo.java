package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class XskInfo extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        io.vproxy.vpxdp.XskRingCons.LAYOUT.withName("rx"),
        io.vproxy.vpxdp.XskRingProd.LAYOUT.withName("tx"),
        ValueLayout.ADDRESS.withName("umem"),
        ValueLayout.ADDRESS.withName("xsk"),
        ValueLayout.JAVA_INT.withName("ifIndex"),
        ValueLayout.JAVA_INT.withName("rxRingSize"),
        ValueLayout.JAVA_INT.withName("txRingSize"),
        ValueLayout.JAVA_INT.withName("outstandingTx"),
        ValueLayout.JAVA_INT.withName("flags"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private final io.vproxy.vpxdp.XskRingCons rx;

    public io.vproxy.vpxdp.XskRingCons getRx() {
        return this.rx;
    }

    private final io.vproxy.vpxdp.XskRingProd tx;

    public io.vproxy.vpxdp.XskRingProd getTx() {
        return this.tx;
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

    public io.vproxy.vpxdp.XskSocket getXsk() {
        var SEG = xskVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new io.vproxy.vpxdp.XskSocket(SEG);
    }

    public void setXsk(io.vproxy.vpxdp.XskSocket xsk) {
        if (xsk == null) {
            xskVH.set(MEMORY, MemorySegment.NULL);
        } else {
            xskVH.set(MEMORY, xsk.MEMORY);
        }
    }

    private static final VarHandleW ifIndexVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("ifIndex")
        )
    );

    public int getIfIndex() {
        return ifIndexVH.getInt(MEMORY);
    }

    public void setIfIndex(int ifIndex) {
        ifIndexVH.set(MEMORY, ifIndex);
    }

    private static final VarHandleW rxRingSizeVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("rxRingSize")
        )
    );

    public int getRxRingSize() {
        return rxRingSizeVH.getInt(MEMORY);
    }

    public void setRxRingSize(int rxRingSize) {
        rxRingSizeVH.set(MEMORY, rxRingSize);
    }

    private static final VarHandleW txRingSizeVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("txRingSize")
        )
    );

    public int getTxRingSize() {
        return txRingSizeVH.getInt(MEMORY);
    }

    public void setTxRingSize(int txRingSize) {
        txRingSizeVH.set(MEMORY, txRingSize);
    }

    private static final VarHandleW outstandingTxVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("outstandingTx")
        )
    );

    public int getOutstandingTx() {
        return outstandingTxVH.getInt(MEMORY);
    }

    public void setOutstandingTx(int outstandingTx) {
        outstandingTxVH.set(MEMORY, outstandingTx);
    }

    private static final VarHandleW flagsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("flags")
        )
    );

    public int getFlags() {
        return flagsVH.getInt(MEMORY);
    }

    public void setFlags(int flags) {
        flagsVH.set(MEMORY, flags);
    }

    public XskInfo(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        this.rx = new io.vproxy.vpxdp.XskRingCons(MEMORY.asSlice(OFFSET, io.vproxy.vpxdp.XskRingCons.LAYOUT.byteSize()));
        OFFSET += io.vproxy.vpxdp.XskRingCons.LAYOUT.byteSize();
        this.tx = new io.vproxy.vpxdp.XskRingProd(MEMORY.asSlice(OFFSET, io.vproxy.vpxdp.XskRingProd.LAYOUT.byteSize()));
        OFFSET += io.vproxy.vpxdp.XskRingProd.LAYOUT.byteSize();
        OFFSET += 8;
        OFFSET += 8;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
    }

    public XskInfo(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    private static final MethodHandle closeMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_xsk_close", MemorySegment.class /* self */);

    public void close() {
        try {
            closeMH.invokeExact(MEMORY);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
    }

    private static final MethodHandle fetchPacketMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_xdp_fetch_pkt", MemorySegment.class /* self */, MemorySegment.class /* idxRxPtr */, MemorySegment.class /* chunkPtrs */);

    public int fetchPacket(IntArray idxRxPtr, PointerArray chunkPtrs) {
        int RESULT;
        try {
            RESULT = (int) fetchPacketMH.invokeExact(MEMORY, (MemorySegment) (idxRxPtr == null ? MemorySegment.NULL : idxRxPtr.MEMORY), (MemorySegment) (chunkPtrs == null ? MemorySegment.NULL : chunkPtrs.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle rxReleaseMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_xdp_rx_release", MemorySegment.class /* self */, int.class /* cnt */);

    public void rxRelease(int cnt) {
        try {
            rxReleaseMH.invokeExact(MEMORY, cnt);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
    }

    private static final MethodHandle writePacketMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_xdp_write_pkt", MemorySegment.class /* self */, io.vproxy.vpxdp.ChunkInfo.LAYOUT.getClass() /* chunk */);

    public int writePacket(io.vproxy.vpxdp.ChunkInfo chunk) {
        int RESULT;
        try {
            RESULT = (int) writePacketMH.invokeExact(MEMORY, (MemorySegment) (chunk == null ? MemorySegment.NULL : chunk.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle writePacketsMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_xdp_write_pkts", MemorySegment.class /* self */, int.class /* size */, MemorySegment.class /* chunkPtrs */);

    public int writePackets(int size, PointerArray chunkPtrs) {
        int RESULT;
        try {
            RESULT = (int) writePacketsMH.invokeExact(MEMORY, size, (MemorySegment) (chunkPtrs == null ? MemorySegment.NULL : chunkPtrs.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle completeTxMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_xdp_complete_tx", MemorySegment.class /* self */);

    public void completeTx() {
        try {
            completeTxMH.invokeExact(MEMORY);
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
        SB.append("XskInfo{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("rx => ");
            PanamaUtils.nativeObjectToString(getRx(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("tx => ");
            PanamaUtils.nativeObjectToString(getTx(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
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
            SB.append(" ".repeat(INDENT + 4)).append("ifIndex => ");
            SB.append(getIfIndex());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("rxRingSize => ");
            SB.append(getRxRingSize());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("txRingSize => ");
            SB.append(getTxRingSize());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("outstandingTx => ");
            SB.append(getOutstandingTx());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("flags => ");
            SB.append(getFlags());
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<XskInfo> {
        public Array(MemorySegment buf) {
            super(buf, XskInfo.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, XskInfo.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, XskInfo.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.XskInfo ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskInfo.Array";
        }

        @Override
        protected XskInfo construct(MemorySegment seg) {
            return new XskInfo(seg);
        }

        @Override
        protected MemorySegment getSegment(XskInfo value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<XskInfo> {
        private Func(io.vproxy.pni.CallSite<XskInfo> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<XskInfo> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<XskInfo> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<XskInfo> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskInfo.Func";
        }

        @Override
        protected XskInfo construct(MemorySegment seg) {
            return new XskInfo(seg);
        }
    }
}
// metadata.generator-version: pni 22.0.0.20
// sha256:24205a36a478c333e3047c8569987c322d7bfbc63662beb43db09c8cbc17e287
