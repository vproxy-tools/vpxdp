package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class UMem extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(

    );
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    public UMem(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
    }

    public UMem(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("UMem{\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<UMem> {
        public Array(MemorySegment buf) {
            super(buf, UMem.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, UMem.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, UMem.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.UMem ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "UMem.Array";
        }

        @Override
        protected UMem construct(MemorySegment seg) {
            return new UMem(seg);
        }

        @Override
        protected MemorySegment getSegment(UMem value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<UMem> {
        private Func(io.vproxy.pni.CallSite<UMem> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<UMem> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<UMem> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<UMem> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "UMem.Func";
        }

        @Override
        protected UMem construct(MemorySegment seg) {
            return new UMem(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:c3753bc5d2a59c6a629123e5367d53a802dd831c504bcfc2c484d76256dac43d
