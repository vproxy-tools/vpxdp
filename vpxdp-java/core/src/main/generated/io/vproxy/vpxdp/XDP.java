package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class XDP {
    private XDP() {
    }

    private static final XDP INSTANCE = new XDP();

    public static XDP get() {
        return INSTANCE;
    }

    private static final MethodHandle attachBPFObjectToIfMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.BPFObject.LAYOUT.getClass(), "vp_bpfobj_attach_to_if", String.class /* filepath */, String.class /* prog */, String.class /* ifname */, int.class /* attachFlags */);

    public io.vproxy.vpxdp.BPFObject attachBPFObjectToIf(PNIString filepath, PNIString prog, PNIString ifname, int attachFlags) {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) attachBPFObjectToIfMH.invokeExact((MemorySegment) (filepath == null ? MemorySegment.NULL : filepath.MEMORY), (MemorySegment) (prog == null ? MemorySegment.NULL : prog.MEMORY), (MemorySegment) (ifname == null ? MemorySegment.NULL : ifname.MEMORY), attachFlags);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.BPFObject(RESULT);
    }

    private static final MethodHandle detachBPFObjectFromIfMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_bpfobj_detach_from_if", String.class /* ifname */);

    public int detachBPFObjectFromIf(PNIString ifname) {
        int RESULT;
        try {
            RESULT = (int) detachBPFObjectFromIfMH.invokeExact((MemorySegment) (ifname == null ? MemorySegment.NULL : ifname.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle createUMemMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.UMemInfo.LAYOUT.getClass(), "vp_umem_create", int.class /* chunksCount */, int.class /* fillRingSize */, int.class /* compRingSize */, int.class /* frameSize */, int.class /* headroom */, int.class /* metaLen */);

    public io.vproxy.vpxdp.UMemInfo createUMem(int chunksCount, int fillRingSize, int compRingSize, int frameSize, int headroom, int metaLen) {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) createUMemMH.invokeExact(chunksCount, fillRingSize, compRingSize, frameSize, headroom, metaLen);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.UMemInfo(RESULT);
    }

    private static final MethodHandle createXskMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), io.vproxy.vpxdp.XskInfo.LAYOUT.getClass(), "vp_xsk_create", String.class /* ifname */, int.class /* queueId */, io.vproxy.vpxdp.UMemInfo.LAYOUT.getClass() /* umem */, int.class /* rxRingSize */, int.class /* txRingSize */, int.class /* xdpFlags */, int.class /* bindFlags */, int.class /* busyPollBudget */, int.class /* vpFlags */);

    public io.vproxy.vpxdp.XskInfo createXsk(PNIString ifname, int queueId, io.vproxy.vpxdp.UMemInfo umem, int rxRingSize, int txRingSize, int xdpFlags, int bindFlags, int busyPollBudget, int vpFlags) {
        MemorySegment RESULT;
        try {
            RESULT = (MemorySegment) createXskMH.invokeExact((MemorySegment) (ifname == null ? MemorySegment.NULL : ifname.MEMORY), queueId, (MemorySegment) (umem == null ? MemorySegment.NULL : umem.MEMORY), rxRingSize, txRingSize, xdpFlags, bindFlags, busyPollBudget, vpFlags);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        if (RESULT.address() == 0) return null;
        return RESULT == null ? null : new io.vproxy.vpxdp.XskInfo(RESULT);
    }
}
// metadata.generator-version: pni 0.0.20
// sha256:9469a510b3a7f4b5a19cc7e6c06eae6d22585ccb7d79f57765f57d7c0d412885
