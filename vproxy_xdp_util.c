#include "vproxy_xdp.h"

#include <errno.h>
#include <string.h>
#include <net/if.h>
#include <linux/if_link.h>
#include <unistd.h>

struct bpf_object* load_bpf_object_file(const char* filename, struct vp_bpf_map_reuse* maps) {
    struct bpf_object_open_opts open_opts = { 0 };
    open_opts.sz = sizeof(struct bpf_object_open_opts);

    struct bpf_object* obj = bpf_object__open_file(filename, &open_opts);
    if (obj == NULL) {
        fprintf(stderr, "ERR: bpf_object__open_file(%s) failed: %d %s\n",
                        filename, errno, strerror(errno));
        return NULL;
    }

    int mapfds[VP_BPF_MAP_REUSE_ARRAY_MAX];
    int mapfd_offset = 0;
    memset(mapfds, 0, sizeof(mapfds));

    if (maps != NULL) {
        for (int i = 0;;++i) {
            struct vp_bpf_map_reuse* m = &maps[i];
            if (m->name == NULL) {
                break;
            }

            if (i >= VP_BPF_MAP_REUSE_ARRAY_MAX) {
                fprintf(stderr, "ERR: reuse maps array len excceds limit: %d", i);
                break;
            }

            int fd;
            if (m->type == VP_BPF_MAP_REUSE_TYPE_MAP) {
                fd = bpf_map__fd(m->map);
                if (fd < 0) {
                    fprintf(stderr, "ERR: bpf_map__fd(%s) failed: %d %s\n",
                                    m->name, -fd, strerror(-fd));
                    continue;
                }
            } else if (m->type == VP_BPF_MAP_REUSE_TYPE_PIN_PATH) {
                fd = bpf_obj_get(m->pinpath);
                if (fd < 0) {
                    fprintf(stderr, "ERR: bpf_obj_get(%s) failed: %d %s\n",
                                    m->pinpath, -fd, strerror(-fd));
                    continue;
                }
                mapfds[mapfd_offset++] = fd;
            } else {
                fprintf(stderr, "ERR: unknown type(%d) for vp_bpf_map_reuse", m->type);
                continue;
            }

            struct bpf_map* target = bpf_object__find_map_by_name(obj, m->name);
            if (target == NULL) {
                fprintf(stderr, "ERR: bpf_object__find_map_by_name(%s, %s) failed: %d %s\n",
                                filename, m->name, errno, strerror(errno));
                continue;
            }
            int err = bpf_map__reuse_fd(target, fd);
            if (err < 0) {
                fprintf(stderr, "ERR: bpf_map__reuse_fd(%s/%s, %d) failed: %d %s\n",
                                filename, m->name, fd, -err, strerror(-err));
                goto fail_release;
            }
        }
    }

    int err = bpf_object__load(obj);
    if (err < 0) {
        fprintf(stderr, "ERR: bpf_object__load(%s) failed: %d %s\n",
                        filename, -err, strerror(-err));
        goto fail_release;
    }
    for (int i = 0; i < mapfd_offset; ++i) close(mapfds[i]);
    return obj;
fail_release:
    for (int i = 0; i < mapfd_offset; ++i) close(mapfds[i]);
    bpf_object__close(obj);
    return NULL;
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
