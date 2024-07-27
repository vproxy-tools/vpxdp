package io.vproxy.vpxdp;

import io.vproxy.pni.annotation.*;

@Struct
class PollFD {
    int fd;
    short events;
    short revents;
}

@Downcall
interface PNIPoll {
    @Name("poll")
    @Style(Styles.critical)
    int poll(@Raw PollFD[] fds, int nfds, int timeout);
}
