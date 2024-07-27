#include "vproxy_xdp.h"

void _vp_dummy(void) {}
#pragma GCC diagnostic ignored "-Wpragmas"
#pragma redefine_extname _vp_dummy vp_dummy
#pragma GCC diagnostic error "-Wpragmas"

struct vp_chunk_info* _vp_chunk_seek(struct vp_chunk_array* chunks, uint64_t addroff) {
    return vp_chunk_seek(chunks, addroff);
}
#pragma redefine_extname _vp_chunk_seek vp_chunk_seek

struct vp_chunk_info* _vp_chunk_fetch(struct vp_chunk_array* chunks) {
    return vp_chunk_fetch(chunks);
}
#pragma redefine_extname _vp_chunk_fetch vp_chunk_fetch

void _vp_chunk_release(struct vp_chunk_array* chunks, struct vp_chunk_info* chunk) {
    vp_chunk_release(chunks, chunk);
}
#pragma redefine_extname _vp_chunk_release vp_chunk_release
