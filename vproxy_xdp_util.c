#include "vproxy_xdp.h"

#include <errno.h>
#include <string.h>
#include <net/if.h>
#include <linux/if_link.h>

struct bpf_object* load_bpf_object_file(const char* filename) {
    struct bpf_object_open_opts open_opts = { 0 };
    open_opts.sz = sizeof(struct bpf_object_open_opts);

    struct bpf_object* obj = bpf_object__open_file(filename, &open_opts);
    if (obj == NULL) {
        fprintf(stderr, "ERR: bpf_object__open_file(%s) failed: %d %s\n",
                        filename, errno, strerror(errno));
        return NULL;
    }
    int err = bpf_object__load(obj);
    if (err < 0) {
        fprintf(stderr, "ERR: bpf_object__load(%s) failed: %d %s\n",
                        filename, errno, strerror(errno));
        bpf_object__close(obj);
        return NULL;
    }
    return obj;
}

int xdp_link_attach(int ifindex, __u32 xdp_flags, int prog_fd) {
    struct bpf_xdp_attach_opts attach_opts = { 0 };
    attach_opts.sz = sizeof(struct bpf_xdp_attach_opts);

    int err = bpf_xdp_attach(ifindex, prog_fd, xdp_flags, &attach_opts);
    if (err == -EEXIST && !(xdp_flags & XDP_FLAGS_UPDATE_IF_NOEXIST)) {
        /* Force mode didn't work, probably because a program of the
         * opposite type is loaded. Let's unload that and try loading
         * again
         */

        __u32 old_flags = xdp_flags;

        xdp_flags &= ~XDP_FLAGS_MODES;
        xdp_flags |= (old_flags & XDP_FLAGS_SKB_MODE) ? XDP_FLAGS_DRV_MODE : XDP_FLAGS_SKB_MODE;
        err = bpf_xdp_detach(ifindex, xdp_flags, &attach_opts);
        if (!err)
            err = bpf_xdp_attach(ifindex, prog_fd, xdp_flags, &attach_opts);
    }
    if (err < 0) {
        switch(-err) {
            case EBUSY:
            case EEXIST:
                fprintf(stderr, "XDP already loaded on device\n");
                break;
            case EOPNOTSUPP:
                fprintf(stderr, "Native-XDP not supported, use skb mode instead\n");
                break;
            default:
                fprintf(stderr, "bpf_set_link_xdp_fd failed: %d %s\n",
                        -err, strerror(-err));
        }
        return 1;
    }
    return 0;
}
