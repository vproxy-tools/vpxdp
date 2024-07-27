package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class XskRing extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("cachedProd"),
        ValueLayout.JAVA_INT.withName("cachedCons"),
        ValueLayout.JAVA_INT.withName("mask"),
        ValueLayout.JAVA_INT.withName("size"),
        ValueLayout.ADDRESS.withName("producer"),
        ValueLayout.ADDRESS.withName("consumer"),
        ValueLayout.ADDRESS.withName("ring"),
        ValueLayout.ADDRESS.withName("flags")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW cachedProdVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("cachedProd")
        )
    );

    public int getCachedProd() {
        return cachedProdVH.getInt(MEMORY);
    }

    public void setCachedProd(int cachedProd) {
        cachedProdVH.set(MEMORY, cachedProd);
    }

    private static final VarHandleW cachedConsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("cachedCons")
        )
    );

    public int getCachedCons() {
        return cachedConsVH.getInt(MEMORY);
    }

    public void setCachedCons(int cachedCons) {
        cachedConsVH.set(MEMORY, cachedCons);
    }

    private static final VarHandleW maskVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("mask")
        )
    );

    public int getMask() {
        return maskVH.getInt(MEMORY);
    }

    public void setMask(int mask) {
        maskVH.set(MEMORY, mask);
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

    private static final VarHandleW producerVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("producer")
        )
    );

    public MemorySegment getProducer() {
        var SEG = producerVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setProducer(MemorySegment producer) {
        if (producer == null) {
            producerVH.set(MEMORY, MemorySegment.NULL);
        } else {
            producerVH.set(MEMORY, producer);
        }
    }

    private static final VarHandleW consumerVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("consumer")
        )
    );

    public MemorySegment getConsumer() {
        var SEG = consumerVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setConsumer(MemorySegment consumer) {
        if (consumer == null) {
            consumerVH.set(MEMORY, MemorySegment.NULL);
        } else {
            consumerVH.set(MEMORY, consumer);
        }
    }

    private static final VarHandleW ringVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("ring")
        )
    );

    public MemorySegment getRing() {
        var SEG = ringVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setRing(MemorySegment ring) {
        if (ring == null) {
            ringVH.set(MEMORY, MemorySegment.NULL);
        } else {
            ringVH.set(MEMORY, ring);
        }
    }

    private static final VarHandleW flagsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("flags")
        )
    );

    public MemorySegment getFlags() {
        var SEG = flagsVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setFlags(MemorySegment flags) {
        if (flags == null) {
            flagsVH.set(MEMORY, MemorySegment.NULL);
        } else {
            flagsVH.set(MEMORY, flags);
        }
    }

    public XskRing(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
    }

    public XskRing(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
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
    }

    public static class Array extends RefArray<XskRing> {
        public Array(MemorySegment buf) {
            super(buf, XskRing.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, XskRing.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, XskRing.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.XskRing ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskRing.Array";
        }

        @Override
        protected XskRing construct(MemorySegment seg) {
            return new XskRing(seg);
        }

        @Override
        protected MemorySegment getSegment(XskRing value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<XskRing> {
        private Func(io.vproxy.pni.CallSite<XskRing> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<XskRing> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<XskRing> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<XskRing> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskRing.Func";
        }

        @Override
        protected XskRing construct(MemorySegment seg) {
            return new XskRing(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:a92773f8110d9def9e335aeadc083434b2c043d0abbc8e88ce6cc7283f1ea2c0
