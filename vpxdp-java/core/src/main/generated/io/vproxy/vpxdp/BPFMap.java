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

    private static final MethodHandle addMac2PortMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_mac2port_add_into_map", MemorySegment.class /* self */, MemorySegment.class /* mac */, String.class /* ifname */);

    public int addMac2Port(MemorySegment mac, PNIString ifname) {
        int RESULT;
        try {
            RESULT = (int) addMac2PortMH.invokeExact(MEMORY, (MemorySegment) (mac == null ? MemorySegment.NULL : mac), (MemorySegment) (ifname == null ? MemorySegment.NULL : ifname.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle lookupMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "bpf_map__lookup_elem", MemorySegment.class /* self */, MemorySegment.class /* key */, long.class /* keySize */, MemorySegment.class /* value */, long.class /* valueSize */, long.class /* flags */);

    public int lookup(MemorySegment key, long keySize, MemorySegment value, long valueSize, long flags) {
        int RESULT;
        try {
            RESULT = (int) lookupMH.invokeExact(MEMORY, (MemorySegment) (key == null ? MemorySegment.NULL : key), keySize, (MemorySegment) (value == null ? MemorySegment.NULL : value), valueSize, flags);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle updateMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "bpf_map__update_elem", MemorySegment.class /* self */, MemorySegment.class /* key */, long.class /* keySize */, MemorySegment.class /* value */, long.class /* valueSize */, long.class /* flags */);

    public int update(MemorySegment key, long keySize, MemorySegment value, long valueSize, long flags) {
        int RESULT;
        try {
            RESULT = (int) updateMH.invokeExact(MEMORY, (MemorySegment) (key == null ? MemorySegment.NULL : key), keySize, (MemorySegment) (value == null ? MemorySegment.NULL : value), valueSize, flags);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle deleteMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "bpf_map__delete_elem", MemorySegment.class /* self */, MemorySegment.class /* key */, long.class /* keySize */);

    public int delete(MemorySegment key, long keySize) {
        int RESULT;
        try {
            RESULT = (int) deleteMH.invokeExact(MEMORY, (MemorySegment) (key == null ? MemorySegment.NULL : key), keySize);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle pinMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "bpf_map__pin", MemorySegment.class /* self */, String.class /* path */);

    public int pin(PNIString path) {
        int RESULT;
        try {
            RESULT = (int) pinMH.invokeExact(MEMORY, (MemorySegment) (path == null ? MemorySegment.NULL : path.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle unpinMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "bpf_map__unpin", MemorySegment.class /* self */, String.class /* path */);

    public int unpin(PNIString path) {
        int RESULT;
        try {
            RESULT = (int) unpinMH.invokeExact(MEMORY, (MemorySegment) (path == null ? MemorySegment.NULL : path.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle getPinPathMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), String.class, "bpf_map__pin_path", MemorySegment.class /* self */);

    public PNIString getPinPath() {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) getPinPathMH.invokeExact(MEMORY);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT.address() == 0 ? null : new PNIString(RESULT);
    }

    private static final MethodHandle getNextKeyMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "bpf_map__get_next_key", MemorySegment.class /* self */, MemorySegment.class /* cur */, MemorySegment.class /* nx */, long.class /* keySize */);

    public int getNextKey(MemorySegment cur, MemorySegment nx, long keySize) {
        int RESULT;
        try {
            RESULT = (int) getNextKeyMH.invokeExact(MEMORY, (MemorySegment) (cur == null ? MemorySegment.NULL : cur), (MemorySegment) (nx == null ? MemorySegment.NULL : nx), keySize);
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
// metadata.generator-version: pni 0.0.20
// sha256:663d2ca06285a42fc7912f20433223c0a3e8fe0dd77b4f002522884ee24401df
