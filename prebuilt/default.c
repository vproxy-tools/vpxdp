#include <linux/bpf.h>
#include <bpf/bpf_helpers.h>

struct {
    __uint(type,        BPF_MAP_TYPE_XSKMAP);
    __uint(max_entries, 128);
    __type(key,         int);
    __type(value,       int);
} xsks_map SEC(".maps");

// this map is usually shared between multiple netifs
struct {
    __uint(type,        BPF_MAP_TYPE_LRU_HASH);
    __uint(max_entries, 65536);
    __type(key,         char[6]);
    __type(value,       __u32);
} mac2port_map SEC(".maps");

// this map is usually shared between multiple netifs
// the map is also managed by kernel when dev is removed
struct {
    __uint(type,        BPF_MAP_TYPE_DEVMAP_HASH);
    __uint(max_entries, 256);
    __type(key,         __u32);
    __type(value,       __u32);
} port2dev_map SEC(".maps");

struct {
    __uint(type,        BPF_MAP_TYPE_LRU_HASH);
    __uint(max_entries, 4096); // large map, just in case the netif is connected to a bridge
    __type(key,         char[6]);
    __type(value,       char); // value is not important
} pass_mac_map SEC(".maps");

struct {
    __uint(type,        BPF_MAP_TYPE_LRU_HASH);
    __uint(max_entries, 65536);
    __type(key,         char[6]);
    __type(value,       int);
} srcmac2count_map SEC(".maps");

inline void pkt_count_by_srcmac(struct xdp_md *ctx) {
    unsigned char* data_end = (unsigned char*) ((long) ctx->data_end);
    unsigned char* data     = (unsigned char*) ((long) ctx->data);
    unsigned char* pos      = data;
    pos += 12;
    if (pos > data_end) {
        return;
    }
    int* cnt_ptr = bpf_map_lookup_elem(&srcmac2count_map, data + 6);
    int cnt;
    if (cnt_ptr == NULL) {
        cnt = 1;
        bpf_map_update_elem(&srcmac2count_map, data + 6, &cnt, 0);
        return;
    }
    cnt = *cnt_ptr;
    *cnt_ptr = cnt + 1;
}

inline int redirect_pkt_by_mac(struct xdp_md *ctx) {
    unsigned char* data_end = (unsigned char*) ((long) ctx->data_end);
    unsigned char* data     = (unsigned char*) ((long) ctx->data);
    unsigned char* pos      = data;
    pos += 14; // dst_mac + src_mac + ether_type
    if (pos > data_end) {
        return XDP_DROP;
    }

    // do not handle vlan
    int ether_type = ((data[12] & 0xff) << 8) | (data[13] & 0xff);
    if (ether_type == 0x8100) {
        return XDP_DROP;
    }

    // dst_mac->port
    __u32* output_iface_ptr = bpf_map_lookup_elem(&mac2port_map, data);
    if (output_iface_ptr == NULL) {
        return XDP_DROP;
    }

    // src_mac->port
    __u32* input_iface_ptr = bpf_map_lookup_elem(&mac2port_map, data + 6);
    if (input_iface_ptr == NULL) {
        return XDP_DROP;
    }

    // src port should match
    __u32 input_iface = *input_iface_ptr;
    if (ctx->ingress_ifindex != input_iface) {
        return XDP_DROP;
    }

    // must not be dst port
    __u32 output_iface = *output_iface_ptr;
    if (ctx->ingress_ifindex == output_iface) {
        return XDP_DROP;
    }

    // statistics and do redirect
    pkt_count_by_srcmac(ctx);
    return bpf_redirect_map(&port2dev_map, output_iface, XDP_DROP);
}

SEC("xdp") int xdp_sock(struct xdp_md *ctx)
{
    unsigned char* data_end = (unsigned char*) ((long) ctx->data_end);
    unsigned char* data     = (unsigned char*) ((long) ctx->data);
    unsigned char* pos = data;
    pos += 6;
    if (pos > data_end) {
        return XDP_DROP;
    }

    if (bpf_map_lookup_elem(&pass_mac_map, data)) {
        return XDP_PASS;
    }

    int redirect_res = redirect_pkt_by_mac(ctx);
    if (redirect_res != XDP_DROP && redirect_res != XDP_ABORTED) {
        return redirect_res;
    }

    return bpf_redirect_map(&xsks_map, ctx->rx_queue_index, XDP_DROP);
}
