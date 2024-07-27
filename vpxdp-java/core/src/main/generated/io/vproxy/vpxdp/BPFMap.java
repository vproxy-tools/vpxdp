package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class BPFMap extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(

    );
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    public BPFMap(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
    }

    public BPFMap(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    private static final MethodHandle addXskMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_xsk_add_into_map", MemorySegment.class /* self */, int.class /* key */, io.vproxy.vpxdp.XskInfo.LAYOUT.getClass() /* xsk */);

    public int addXsk(int key, io.vproxy.vpxdp.XskInfo xsk) {
        int RESULT;
        try {
            RESULT = (int) addXskMH.invokeExact(MEMORY, key, (MemorySegment) (xsk == null ? MemorySegment.NULL : xsk.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle addMacMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_mac_add_into_map", MemorySegment.class /* self */, MemorySegment.class /* mac */, String.class /* ifname */);

    public int addMac(MemorySegment mac, PNIString ifname) {
        int RESULT;
        try {
            RESULT = (int) addMacMH.invokeExact(MEMORY, (MemorySegment) (mac == null ? MemorySegment.NULL : mac), (MemorySegment) (ifname == null ? MemorySegment.NULL : ifname.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle removeMacMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_mac_remove_from_map", MemorySegment.class /* self */, MemorySegment.class /* mac */);

    public int removeMac(MemorySegment mac) {
        int RESULT;
        try {
            RESULT = (int) removeMacMH.invokeExact(MEMORY, (MemorySegment) (mac == null ? MemorySegment.NULL : mac));
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
        SB.append("BPFMap{\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<BPFMap> {
        public Array(MemorySegment buf) {
            super(buf, BPFMap.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, BPFMap.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, BPFMap.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.BPFMap ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFMap.Array";
        }

        @Override
        protected BPFMap construct(MemorySegment seg) {
            return new BPFMap(seg);
        }

        @Override
        protected MemorySegment getSegment(BPFMap value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<BPFMap> {
        private Func(io.vproxy.pni.CallSite<BPFMap> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<BPFMap> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFMap> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<BPFMap> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "BPFMap.Func";
        }

        @Override
        protected BPFMap construct(MemorySegment seg) {
            return new BPFMap(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:19a844af4853c26c65bfb7e29fe5590c1e0ba5383438b965842fadb603a045bd
