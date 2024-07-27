# vpxdp

## build

```shell
make so
```

You will get `./vpxdp.so` and `./submodules/xdp-tools/lib/libxdp/libxdp.so`

> Note: the `libbpf.so` is also built, but is not required.

## sample

The sample program captures ipv6 icmp requests, and send back ipv6 icmp replies.

If you are using a non-Linux platform, you can use a privileged docker to run the sample program:

```shell
make docker-run

# to exec into the container from another terminal, run:
docker exec -it vpxdp-sample /bin/bash 
```

To prepare the environment, run only once:

```shell
make prepare
```

To run the sample:

```shell
make run veth0
```

Testing from another terminal:

```shell
ping 'fd00::1'
```
