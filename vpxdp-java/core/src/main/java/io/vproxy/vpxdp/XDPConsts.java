package io.vproxy.vpxdp;

public class XDPConsts {
    private XDPConsts() {
    }

    public static final int XDP_FLAGS_UPDATE_IF_NOEXIST = (1);
    public static final int XDP_FLAGS_SKB_MODE = (1 << 1);
    public static final int XDP_FLAGS_DRV_MODE = (1 << 2);
    public static final int XDP_FLAGS_HW_MODE = (1 << 3);

    public static final int XDP_COPY = (1 << 1);
    public static final int XDP_ZEROCOPY = (1 << 2);
    public static final int XDP_USE_NEED_WAKEUP = (1 << 3);

    public static final int VP_CSUM_NO = (0);
    public static final int VP_CSUM_IP = (1);
    public static final int VP_CSUM_UP = (1 << 1);
    public static final int VP_CSUM_UP_PSEUDO = (1 << 2);
    public static final int VP_CSUM_ALL = (VP_CSUM_UP | VP_CSUM_IP);
    public static final int VP_CSUM_XDP_OFFLOAD = (1 << 16);

    public static final int VP_BPF_MAP_REUSE_TYPE_MAP = 1;
    public static final int VP_BPF_MAP_REUSE_TYPE_PIN_PATH = 2;

    public static final short POLLIN = 1;
    public static final short POLLOUT = 4;

    public static final int ENOENT = 2;
}
