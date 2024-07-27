#ifndef VPROXY_XDP_H
#define VPROXY_XDP_H

#include <stdint.h>

#include <bpf/libbpf.h>
#include <xdp/xsk.h>
#include <xdp/libxdp.h>

struct vp_umem_info;
struct vp_xsk_info;

struct vp_chunk_info {
    struct vp_umem_info* umem;
    struct vp_xsk_info*  xsk;

    uint8_t  ref;      // reference count, 0 means it can be used by the rings
    uint8_t  tx_ref;   // increase when put into tx ring
                       // decrease when fetched from comp ring
    uint64_t addr;     // xsk addr, offset of this chunk
    uint64_t endaddr;  // xsk addr, offset of the end cursor (exclusive) of this chunk
    char*    realaddr; // pointer to the real mem location of this chunk

    uint64_t pktaddr; // xsk addr, used when sending and receiving
    char*    pkt;     // used when receiving, pointer to the real mem of the packet
    uint32_t pktlen;  // xsk addr, used when sending and receiving

    int csum_flags; // used when sending for calculating checksums
};

struct vp_chunk_array {
    int frame_count;
    int size;
    int used;
    int idx; // for fetching chunks
    struct vp_chunk_info* array;
};

struct vp_umem_info {
    struct xsk_ring_prod fill_ring;
    struct xsk_ring_cons comp_ring;
    struct xsk_umem*     umem;

    int is_shared;

    char* buffer;
    int buffer_size;

    struct vp_chunk_array* chunks;
    int tx_metadata_len;
};

struct vp_xsk_info {
    struct xsk_ring_cons rx;
    struct xsk_ring_prod tx;
    struct vp_umem_info* umem;
    struct xsk_socket*   xsk;

    int ifindex;

    int rx_ring_size;
    int tx_ring_size;

    int outstanding_tx;

#define VP_XSK_FLAG_RX_GEN_CSUM (1)
    int flags;
};

struct bpf_object* vp_bpfobj_attach_to_if    (char* filepath, char* prog, char* ifname, int attach_flags);
int                vp_bpfobj_detach_from_if  (char* ifname);
struct bpf_map*    vp_bpfobj_find_map_by_name(struct bpf_object* bpfobj, char* name);

struct vp_umem_info* vp_umem_create(int chunks_size, int fill_ring_size, int comp_ring_size,
                                    uint64_t frame_count, int headroom, int meta_len);
struct vp_umem_info* vp_umem_share (struct vp_umem_info* umem);
struct vp_xsk_info*  vp_xsk_create (char* ifname, int queue_id, struct vp_umem_info* umem,
                                    int rx_ring_size, int tx_ring_size, int xdp_flags, int bind_flags,
                                    int busy_poll_budget, int vp_flags);

int vp_xsk_add_into_map(struct bpf_map* map, int key, struct vp_xsk_info* xsk);
int vp_mac_add_into_map(struct bpf_map* map, char* mac, char* ifname);
int vp_mac_remove_from_map(struct bpf_map* map, char* mac);

void vp_xsk_close (struct vp_xsk_info* xsk);
void vp_umem_close(struct vp_umem_info* umem, bool clean_buffer);

void vp_xdp_fill_ring_fillup(struct vp_umem_info* umem);
// the first invocation to this function, idx_rx should be set to -1 (all bits 1)
// `idx_rx` will be set in each iteration and user may not modify the value.
// the first invocation returns received packet count,
// user should iterate [return value] times.
// if idx_rx is not -1, return value will always be 0
// otherwise, returning 0 indicates that there's no packets
// also, when idx_rx is -1, the chunk will not be set, and packets will not be retrieved
int  vp_xdp_fetch_pkt  (struct vp_xsk_info* xsk, uint32_t* idx_rx_ptr, struct vp_chunk_info** chunkptr);
void vp_xdp_rx_release (struct vp_xsk_info* xsk, int cnt);
int  vp_xdp_write_pkt  (struct vp_xsk_info* xsk, struct vp_chunk_info* chunk);
int  vp_xdp_write_pkts (struct vp_xsk_info* xsk, int size, struct vp_chunk_info** chunk_ptrs);
void vp_xdp_complete_tx(struct vp_xsk_info* xsk);

void vp_bpfobj_release(struct bpf_object* bpf_obj);

static inline struct vp_chunk_info* vp_chunk_seek(struct vp_chunk_array* chunks, uint64_t addroff) {
    return &chunks->array[addroff / chunks->frame_count];
}

static inline struct vp_chunk_info* vp_chunk_fetch(struct vp_chunk_array* chunks) {
    if (chunks->size == chunks->used) return NULL;
    int last = chunks->idx;
    while (1) {
        struct vp_chunk_info* info = &chunks->array[chunks->idx++];
        if (chunks->idx == chunks->size) {
            chunks->idx = 0;
        }
        if (info->ref == 0) {
            info->ref = 1;
            chunks->used++;
            info->csum_flags = 0;
            return info;
        }
        if (chunks->idx == last) { // runs a whole loop
            break;
        }
    }
    // should not reach here
    fprintf(stderr, "WARN: chunks->size %d != chunks->used %d, but chunk not retrieved\n",
            chunks->size, chunks->used);
    return NULL;
}

static inline void vp_chunk_release(struct vp_chunk_array* chunks, struct vp_chunk_info* chunk) {
    if (__builtin_expect(chunk->ref == 0, 0)) {
        fprintf(stderr, "WARN: chunk %lu in array %lu ref count is 0 before trying to release it, chunks->used = %d, chunks->size = %d\n", (size_t) chunk, (size_t) chunks, chunks->used, chunks->size);
        return;
    }
    if (chunk->ref == 1) {
        chunks->used--;
    }
    chunk->ref--;
}

#endif
