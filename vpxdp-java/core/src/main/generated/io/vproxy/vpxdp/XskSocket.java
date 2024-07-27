package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class XskSocket extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(

    );
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    public XskSocket(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
    }

    public XskSocket(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    private static final MethodHandle fdMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "xsk_socket__fd", MemorySegment.class /* self */);

    public int fd() {
        int RESULT;
        try {
            RESULT = (int) fdMH.invokeExact(MEMORY);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("XskSocket{\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<XskSocket> {
        public Array(MemorySegment buf) {
            super(buf, XskSocket.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, XskSocket.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, XskSocket.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.XskSocket ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskSocket.Array";
        }

        @Override
        protected XskSocket construct(MemorySegment seg) {
            return new XskSocket(seg);
        }

        @Override
        protected MemorySegment getSegment(XskSocket value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<XskSocket> {
        private Func(io.vproxy.pni.CallSite<XskSocket> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<XskSocket> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<XskSocket> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<XskSocket> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "XskSocket.Func";
        }

        @Override
        protected XskSocket construct(MemorySegment seg) {
            return new XskSocket(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:e44f14fa9f18341cfd2a2f12f5676a25c462b53e2b087d50b0735fcad78a32ab
