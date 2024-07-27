#include "vproxy_xdp.h"

#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <unistd.h>
#include <string.h>
#include <linux/if_link.h>
#include <poll.h>

#include "vproxy_checksum.h"

// Usage:
//     hexDump(desc, addr, len, perLine);
//         desc:    if non-NULL, printed as a description before hex dump.
//         addr:    the address to start dumping from.
//         len:     the number of bytes to dump.
//         perLine: number of bytes on each output line.

// hexDump is copied from https://stackoverflow.com/questions/7775991/how-to-get-hexdump-of-a-structure-data
void hexDump (
    const char * desc,
    const void * addr,
    const int len,
    const int perLine
) {
    int i;
    unsigned char buff[perLine+1];
    const unsigned char * pc = (const unsigned char *)addr;

    // Output description if given.

    if (desc != NULL) printf ("%s:\n", desc);

    // Length checks.

    if (len == 0) {
        printf("  ZERO LENGTH\n");
        return;
    }
    if (len < 0) {
        printf("  NEGATIVE LENGTH: %d\n", len);
        return;
    }

    // Process every byte in the data.

    for (i = 0; i < len; i++) {
        // Multiple of perLine means new or first line (with line offset).

        if ((i % perLine) == 0) {
            // Only print previous-line ASCII buffer for lines beyond first.

            if (i != 0) printf ("  %s\n", buff);

            // Output the offset of current line.

            printf ("  %04x ", i);
        }

        // Now the hex code for the specific character.

        printf (" %02x", pc[i]);

        // And buffer a printable ASCII character for later.

        if ((pc[i] < 0x20) || (pc[i] > 0x7e)) // isprint() may be better.
            buff[i % perLine] = '.';
        else
            buff[i % perLine] = pc[i];
        buff[(i % perLine) + 1] = '\0';
    }

    // Pad out last line if not exactly perLine characters.

    while ((i % perLine) != 0) {
        printf ("   ");
        i++;
    }

    // And print the final ASCII buffer.

    printf ("  %s\n", buff);
}

volatile sig_atomic_t sigint = 0;
void handleSigInt(int signal) {
    sigint = 1;
}

int main(int argc, char** argv) {
    if (argc < 2) {
        printf("the first argument should be ifname to attach the ebpf program\n");
        return 1;
    }
    char* ifname = argv[1];

#define XDP_MODE (XDP_FLAGS_SKB_MODE)
    struct bpf_object* bpfobj = vp_bpfobj_attach_to_if("sample_kern.o", "xdp_sock", ifname, XDP_MODE);
    if (bpfobj == NULL) {
        return 1;
    }
    struct bpf_map* map = vp_bpfobj_find_map_by_name(bpfobj, "xsks_map");
    if (map == NULL) {
        return 1;
    }

#define HEADROOM (512)
#define META_LEN (32)
    struct vp_umem_info* umem = vp_umem_create(64, 32, 32, XSK_UMEM__DEFAULT_FRAME_SIZE, HEADROOM, META_LEN);
    if (umem == NULL) {
        return 1;
    }
    struct vp_xsk_info* xsk = vp_xsk_create(ifname, 0, umem, 32, 32, XDP_MODE, XDP_COPY, 0, 0);
    if (xsk == NULL) {
        return 1;
    }
    int ret = vp_xsk_add_into_map(map, 0, xsk);
    if (ret) {
        return 1;
    }

    vp_bpfobj_release(bpfobj);

    printf("ready to poll\n");

    struct pollfd fds[2];
    memset(fds, 0, sizeof(fds));
    fds[0].fd = xsk_socket__fd(xsk->xsk);
    fds[0].events = POLLIN;

    signal(SIGINT, handleSigInt);

    printf("press ctrl-c to exit\n");
    int cnt = 0;
    while (1) {
        if (sigint) break;
        int ret = poll(fds, 1, 50);
        if (ret <= 0 || ret > 1) {
            if (sigint) break;
            continue;
        }

        printf("poll triggered\n");

        int32_t idx_rx = -1;
        struct vp_chunk_info* chunk;
        int rcvd = vp_xdp_fetch_pkt(xsk, &idx_rx, &chunk);
        if (rcvd == 0) {
            continue;
        }
        printf("rcvd = %d\n", rcvd);
        for (int i = 0; i < rcvd; ++i) {
            vp_xdp_fetch_pkt(xsk, &idx_rx, &chunk);
            printf("received packet: cnt=%d addr=%lu pkt=%lu len=%d umem_size=%d umem_used=%d\n",
                   ++cnt, chunk->addr, ((size_t) chunk->pkt) - ((size_t) umem->buffer), chunk->pktlen,
                   umem->chunks->size, umem->chunks->used);
            hexDump("received", chunk->pkt, chunk->pktlen, 16);

            if (chunk->pktlen >= 14 + 40 /* xdp-tutorial uses ipv6 only */ + 1 /* icmp type */
                && (((chunk->pkt[12] & 0xff) << 8) | (chunk->pkt[13] & 0xff)) == 0x86dd
                && (chunk->pkt[14 + 6] & 0xff) == 58) {
                // swap mac dst and src
                for (int i = 0; i < 6; ++i) {
                    char b = chunk->pkt[i];
                    chunk->pkt[i] = chunk->pkt[6 + i];
                    chunk->pkt[6 + i] = b;
                }
                // swap ipv6 src and dst
                for (int i = 0; i < 16; ++i) {
                    char b = chunk->pkt[14 + 8 + i];
                    chunk->pkt[14 + 8 + i] = chunk->pkt[14 + 8 + 16 + i];
                    chunk->pkt[14 + 8 + 16 + i] = b;
                }
                // set icmp type
                chunk->pkt[14 + 40] = 129;
                struct vp_chunk_info* s_chunk;
                int is_copy = (cnt % 2) == 1;
                int is_offload = (cnt % 4) >= 2;
                if (!is_copy) {
                    chunk->ref++;
                    s_chunk = chunk;
                } else {
                    struct vp_chunk_info* chunk2 = vp_chunk_fetch(umem->chunks);
                    if (chunk2 == NULL) {
                        printf("ERR! umem no enough chunks: size=%d used=%d\n",
                               umem->chunks->size, umem->chunks->used);
                        continue;
                    }
                    chunk2->pktaddr = chunk2->addr + META_LEN;
                    chunk2->pkt = chunk2->realaddr + META_LEN;
                    chunk2->pktlen = chunk->pktlen;
                    memcpy(chunk2->pkt, chunk->pkt, chunk->pktlen);

                    s_chunk = chunk2;
                }
                if (!is_offload) {
                    s_chunk->csum_flags = VP_CSUM_ALL;
                } else {
                    s_chunk->csum_flags = VP_CSUM_IP | VP_CSUM_UP_PSEUDO | VP_CSUM_XDP_OFFLOAD;
                }
#define TEXT_GREEN ("\033[0;32m")
#define TEXT_RED   ("\033[0;31m")
                printf("no_copy = %s%s\033[0m , csum_offload = %s%s\033[0m\n",
                       (is_copy    ? TEXT_RED : TEXT_GREEN), (is_copy     ? "off" : "on"),
                       (is_offload ? TEXT_GREEN : TEXT_RED), (is_offload ? "on" : "off"));
                vp_xdp_write_pkt(xsk, s_chunk);
            }

            vp_chunk_release(umem->chunks, chunk);
        }
        vp_xdp_rx_release(xsk, rcvd);
        vp_xdp_fill_ring_fillup(umem);
        vp_xdp_complete_tx(xsk);
    }
    vp_xsk_close(xsk);
    vp_umem_close(umem, true);
    vp_bpfobj_detach_from_if(ifname);

    printf("received %d packets, exit\n", cnt);
    return 0;
}
