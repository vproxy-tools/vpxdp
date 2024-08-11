.DEFAULT_GOAL = all

XDPTOOLS_PATH = ../submodules/xdp-tools

LIBBPF_INC = $(XDPTOOLS_PATH)/lib/libbpf/src/root/usr/include
LIBXDP_INC = $(XDPTOOLS_PATH)/headers
UAPI_INC   = $(XDPTOOLS_PATH)/lib/libbpf/include/uapi
linux-gnu-include := $(shell echo /usr/include/`uname -m`-linux-gnu)
INC_CMD = -I $(UAPI_INC) -I $(LIBBPF_INC) -I $(LIBXDP_INC) -I $(linux-gnu-include)

.PHONY: all
all: default

.PHONY: clean
clean:
	rm -f default.ll
	rm -f default.o

.PHONY: xdptools
xdptools:
	cd ../ && make xdptools

.PHONY: default
default:
	rm -f default.o
	clang -g -O2 $(INC_CMD) -target bpf -S -c -emit-llvm -o default.ll default.c
	llc -march=bpf -filetype=obj -o default.o default.ll
