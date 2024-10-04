#include <linux/bpf.h>
#include <bpf/bpf_helpers.h>

struct {
    __uint(type,        BPF_MAP_TYPE_XSKMAP);
    __uint(max_entries, 128);
    __type(key,         int);
    __type(value,       int);
} xsks_map SEC(".maps");

struct {
    __uint(type,        BPF_MAP_TYPE_ARRAY);
    __uint(max_entries, 65536);
    __type(key,         int);
    __type(value,       char);
} handled_ports SEC(".maps");

#define ETHER_TYPE_OFF  (12)
#define IP4_OFF         (14)
#define IP4_IHL_OFF     (IP4_OFF)
#define IP4_PROTO_OFF   (IP4_OFF  + 9)
#define TCPUDP4_OFF     (IP4_OFF  + 20)
#define TCPUDP4_DST_OFF (TCPUDP4_OFF + 2)
#define IP6_OFF         (14)
#define IP6_PROTO_OFF   (IP6_OFF  + 6)
#define TCPUDP6_OFF     (IP6_OFF  + 40)
#define TCPUDP6_DST_OFF (TCPUDP6_OFF + 2)

#define ETHER_TYPE_IPv4 (0x0800)
#define ETHER_TYPE_IPv6 (0x86dd)
#define IP_PROTO_TCP    (6)
#define IP_PROTO_UDP    (17)

SEC("xdp") int xdp_sock(struct xdp_md *ctx)
{
    unsigned char* data_end = (unsigned char*) ((long) ctx->data_end);
    unsigned char* data     = (unsigned char*) ((long) ctx->data);
    unsigned char* pos = data;
    pos += ETHER_TYPE_OFF + 2;
    if (pos > data_end) {
        return XDP_PASS;
    }
    int ether_type = ((data[ETHER_TYPE_OFF] & 0xff) << 8) | (data[ETHER_TYPE_OFF + 1] & 0xff);
    if (ether_type == ETHER_TYPE_IPv4) {
        pos = data + IP4_IHL_OFF + 1;
        if (pos > data_end) {
            return XDP_PASS;
        }
        int ip_len = data[IP4_IHL_OFF] & 0xf;
        if (ip_len != 5) {
            return XDP_PASS;
        }
        pos = data + IP4_PROTO_OFF + 1;
        if (pos > data_end) {
            return XDP_PASS;
        }
        int proto = data[IP4_PROTO_OFF] & 0xff;
        if (proto != IP_PROTO_TCP && proto != IP_PROTO_UDP) {
            return XDP_PASS;
        }
        pos = data + TCPUDP4_DST_OFF + 2;
        if (pos > data_end) {
            return XDP_PASS;
        }
        int dst = ((data[TCPUDP4_DST_OFF] & 0xff) << 8) | (data[TCPUDP4_DST_OFF + 1] & 0xff);
        if (bpf_map_lookup_elem(&handled_ports, &dst) == NULL) {
            return XDP_PASS;
        }
    } else if (ether_type == ETHER_TYPE_IPv6) {
        pos = data + IP6_PROTO_OFF + 1;
        if (pos > data_end) {
            return XDP_PASS;
        }
        int proto = data[IP6_PROTO_OFF] & 0xff;
        if (proto != IP_PROTO_TCP && proto != IP_PROTO_UDP) {
            return XDP_PASS;
        }
        pos = data + TCPUDP6_DST_OFF + 2;
        if (pos > data_end) {
            return XDP_PASS;
        }
        int dst = ((data[TCPUDP6_DST_OFF] & 0xff) << 8) | (data[TCPUDP6_DST_OFF + 1] & 0xff);
        if (bpf_map_lookup_elem(&handled_ports, &dst) == NULL) {
            return XDP_PASS;
        }
    } else {
        return XDP_PASS;
    }
    return bpf_redirect_map(&xsks_map, ctx->rx_queue_index, XDP_PASS);
}
