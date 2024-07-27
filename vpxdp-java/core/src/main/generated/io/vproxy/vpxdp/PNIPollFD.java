package io.vproxy.vpxdp;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;

public class PNIPollFD extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("fd"),
        ValueLayout.JAVA_SHORT.withName("events"),
        ValueLayout.JAVA_SHORT.withName("revents")
    ).withByteAlignment(4);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW fdVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("fd")
        )
    );

    public int getFd() {
        return fdVH.getInt(MEMORY);
    }

    public void setFd(int fd) {
        fdVH.set(MEMORY, fd);
    }

    private static final VarHandleW eventsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("events")
        )
    );

    public short getEvents() {
        return eventsVH.getShort(MEMORY);
    }

    public void setEvents(short events) {
        eventsVH.set(MEMORY, events);
    }

    private static final VarHandleW reventsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("revents")
        )
    );

    public short getRevents() {
        return reventsVH.getShort(MEMORY);
    }

    public void setRevents(short revents) {
        reventsVH.set(MEMORY, revents);
    }

    public PNIPollFD(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_SHORT_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_SHORT_UNALIGNED.byteSize();
    }

    public PNIPollFD(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("PNIPollFD{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("fd => ");
            SB.append(getFd());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("events => ");
            SB.append(getEvents());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("revents => ");
            SB.append(getRevents());
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<PNIPollFD> {
        public Array(MemorySegment buf) {
            super(buf, PNIPollFD.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, PNIPollFD.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, PNIPollFD.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.vpxdp.PNIPollFD ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "PNIPollFD.Array";
        }

        @Override
        protected PNIPollFD construct(MemorySegment seg) {
            return new PNIPollFD(seg);
        }

        @Override
        protected MemorySegment getSegment(PNIPollFD value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<PNIPollFD> {
        private Func(io.vproxy.pni.CallSite<PNIPollFD> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<PNIPollFD> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<PNIPollFD> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<PNIPollFD> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "PNIPollFD.Func";
        }

        @Override
        protected PNIPollFD construct(MemorySegment seg) {
            return new PNIPollFD(seg);
        }
    }
}
// metadata.generator-version: pni 21.0.0.20
// sha256:2567b0d96c8e292472c11a80c2ccb95f1eccfb0f654e688b04eb405dcf944b27
