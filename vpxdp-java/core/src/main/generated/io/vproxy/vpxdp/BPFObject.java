package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class BPFObject extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(

    );
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    public BPFObject(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
    }

    public BPFObject(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    private static final MethodHandle findMapByNameMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.BPFMap.LAYOUT.getClass(), "vp_bpfobj_find_map_by_name", MemorySegment.class /* self */, String.class /* name */);

    public io.vproxy.vpxdp.BPFMap findMapByName(PNIString name) {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) findMapByNameMH.invokeExact(MEMORY, (MemorySegment) (name == null ? MemorySegment.NULL : name.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.BPFMap(RESULT);
    }

    private static final MethodHandle releaseMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), void.class, "vp_bpfobj_release", MemorySegment.class /* self */);

    public void release() {
        try {
            releaseMH.invokeExact(MEMORY);
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
        SB.append("BPFObject{\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<BPFObject> {
        public Array(MemorySegment buf) {
            super(buf, BPFObject.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, BPFObject.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, BPFObject.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.BPFObject ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFObject.Array";
        }

        @Override
        protected BPFObject construct(MemorySegment seg) {
            return new BPFObject(seg);
        }

        @Override
        protected MemorySegment getSegment(BPFObject value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<BPFObject> {
        private Func(io.vproxy.pni.CallSite<BPFObject> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<BPFObject> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFObject> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFObject> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFObject.Func";
        }

        @Override
        protected BPFObject construct(MemorySegment seg) {
            return new BPFObject(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:5b75749532378fa37490446db3c39fbd4cddaa980418585cd54bad27287518f4
