.DEFAULT_GOAL = all

XDPTOOLS_PATH = ./submodules/xdp-tools

LIBBPF_INC = $(XDPTOOLS_PATH)/lib/libbpf/src/root/usr/include
LIBXDP_INC = $(XDPTOOLS_PATH)/headers
UAPI_INC   = $(XDPTOOLS_PATH)/lib/libbpf/include/uapi
linux-gnu-include := $(shell echo /usr/include/`uname -m`-linux-gnu)
INC_CMD = -I $(UAPI_INC) -I $(LIBBPF_INC) -I $(LIBXDP_INC) -I $(linux-gnu-include)

LIBXDP_LD  = $(XDPTOOLS_PATH)/lib/libxdp
linux-gnu-lib = $(shell echo /usr/lib/`uname -m`-linux-gnu)
LD_CMD = -L $(LIBXDP_LD) -L $(linux-gnu-lib)
LD_PATH = $(LIBXDP_LD):$(linux-gnu-lib):.

.PHONY: clean
clean:
	cd $(XDPTOOLS_PATH) && make clean
	rm -f sample_user
	rm -f sample_kern.ll
	rm -f sample_kern.o
	rm -f libvpxdp.so
	cd vpxdp-java && ./gradlew clean

.PHONY: xdptools
xdptools:
	cd $(XDPTOOLS_PATH) && make

.PHONY: sample_kern
sample_kern: xdptools
	rm -f sample_kern.o
	clang -g -O2 $(INC_CMD) -target bpf -S -c -emit-llvm -o sample_kern.ll sample_kern.c
	llc -march=bpf -filetype=obj -o sample_kern.o sample_kern.ll

.PHONY: sample_user
sample_user: xdptools so
	gcc -O2 $(INC_CMD) $(LD_CMD) -I . -L . \
		-g -o sample_user sample_user.c \
		-lxdp -lelf -lvpxdp

.PHONY: so
so: xdptools
	rm -f libvpxdp.so
	gcc -O2 $(INC_CMD) $(LD_CMD) \
		-g -o libvpxdp.so -fPIC -shared \
		vproxy_xdp.c vproxy_xdp_util.c vproxy_checksum.c expose_inline.c \
		-lxdp -lelf

.PHONY: all
all: so sample_user sample_kern

.PHONY: prepare
prepare:
	ip link add veth0 type veth peer name veth1
	ip link set veth0 up
	ip link set veth1 up
	ip addr add 'fd00::2/120' dev veth1
	ip neigh add 'fd00::1' dev veth1 lladdr '11:22:33:aa:bb:cc'

.PHONY: run
run: sample_kern sample_user
	LD_LIBRARY_PATH=$(LD_PATH) ./sample_user $(filter-out $@,$(MAKECMDGOALS))

.PHONY: run-java
run-java: so sample_kern
	cd vpxdp-java && ./gradlew runSample --args=$(filter-out $@,$(MAKECMDGOALS))

.PHONY: docker-run
docker-run:
	docker run --name=vpxdp-sample --rm \
		--net=host --privileged -it -v `pwd`:/vproxy \
		vproxyio/compile:latest /bin/bash
