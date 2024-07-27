package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class Poll {
    private Poll() {
    }

    private static final Poll INSTANCE = new Poll();

    public static Poll get() {
        return INSTANCE;
    }

    private static final MethodHandle pollMH = PanamaUtils.lookupPNICriticalFunction(new PNILinkOptions(), int.class, "poll", MemorySegment.class /* fds */, int.class /* nfds */, int.class /* timeout */);

    public int poll(io.vproxy.vpxdp.PNIPollFD.Array fds, int nfds, int timeout) {
        int RESULT;
        try {
            RESULT = (int) pollMH.invokeExact((MemorySegment) (fds == null ? MemorySegment.NULL : fds.MEMORY), nfds, timeout);
        } catch (Throwable THROWABLE) {
            throw PanamaUtils.convertInvokeExactException(THROWABLE);
        }
        return RESULT;
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:cc4e33f887844ac1beb46b47e5b73c73cf5db61936a32e6eaa36e70573911db5
