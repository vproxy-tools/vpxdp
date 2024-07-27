package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class CheckSum {
    private CheckSum() {
    }

    private static final CheckSum INSTANCE = new CheckSum();

    public static CheckSum get() {
        return INSTANCE;
    }

    private static final MethodHandle etherMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_pkt_ether_csum", MemorySegment.class /* raw */, int.class /* len */, int.class /* flags */);

    public int ether(MemorySegment raw, int len, int flags) {
        int RESULT;
        try {
            RESULT = (int) etherMH.invokeExact((MemorySegment) (raw == null ? MemorySegment.NULL : raw), len, flags);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }

    private static final MethodHandle etherExMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions().setCritical(true), int.class, "vp_pkt_ether_csum_ex", MemorySegment.class /* raw */, int.class /* len */, int.class /* flags */, io.vproxy.vpxdp.ChecksumOut.LAYOUT.getClass() /* out */);

    public int etherEx(MemorySegment raw, int len, int flags, io.vproxy.vpxdp.ChecksumOut out) {
        int RESULT;
        try {
            RESULT = (int) etherExMH.invokeExact((MemorySegment) (raw == null ? MemorySegment.NULL : raw), len, flags, (MemorySegment) (out == null ? MemorySegment.NULL : out.MEMORY));
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:94afc0fbbd0d342566eca5f09939dbb83646234cd9b4b8a4fcb255c0954f5760
