# Install

0. Prereequisites: `sudo apt install erofs-utils`
1. Install [repo](https://gerrit.googlesource.com/git-repo/)
3. Create a directory: `mkdir yocto-qemu-arm-playground && cd yocto-qemu-arm-playground`
3. Clone with: `repo init -u git@github.com:lucasdietrich/yocto-qemu-arm-playground.git -b main -m default.xml`
4. Sync with: `repo sync`
6. Install yocto project prerequisites: <https://docs.yoctoproject.org/ref-manual/system-requirements.html>
7. Source the environment: `source poky/oe-init-build-env`
8. Configure `local.conf`

```conf
MACHINE ?= "qemuarm"
DISTRO ?= "lux"
```

9. Configure `bblayers.conf`

```conf
BBLAYERS ?= " \
  /home/lucas/projects/yocto-qemu-arm/poky/meta \
  /home/lucas/projects/yocto-qemu-arm/poky/meta-poky \
  /home/lucas/projects/yocto-qemu-arm/poky/meta-yocto-bsp \
  /home/lucas/projects/yocto-qemu-arm/meta-openembedded/meta-oe \
  /home/lucas/projects/yocto-qemu-arm/meta-openembedded/meta-networking \
  /home/lucas/projects/yocto-qemu-arm/meta-openembedded/meta-filesystems \
  /home/lucas/projects/yocto-qemu-arm/meta-openembedded/meta-webserver \
  /home/lucas/projects/yocto-qemu-arm/meta-openembedded/meta-python \
  /home/lucas/projects/yocto-qemu-arm/meta-arm/meta-arm \
  /home/lucas/projects/yocto-qemu-arm/meta-arm/meta-arm-toolchain \
  /home/lucas/projects/yocto-qemu-arm/meta-virtualization \
  /home/lucas/projects/yocto-qemu-arm/meta-playground \
  "
```

10. Build default image: `bitbake image-sandbox`
11. Run qemu: `runqemu qemuarm nographic`
12. Build the SDK: `bitbake -c populate_sdk image-sandbox`

## Tested machines

- `MACHINE = "qemuarm"`
- `MACHINE = "qemuarm-secureboot"`

## Test OP-TEE

## Build and run container base image:

Build the base image with:

```bash
bitbake image-container-base
```

Run 

```bash
mkdir -p oci-bundle
umoci unpack --rootless --image ../build/tmp/deploy/images/qemuarm/image-container-base-latest-oci:latest oci-bundle
fakeroot sh -c "mkfs.erofs -zlz4hc --all-root oci-bundle.img oci-bundle"
scp oci-bundle.img root@192.168.7.2:/home/root
ssh root@192.168.7.2
mkdir -p ~/oci-bundle
mount -o loop oci-bundle.img /home/root/oci-bundle
mkdir -p /tmp/{upper,work,merged}
mount -t overlay overlay -o lowerdir=/home/root/oci-bundle/rootfs,upperdir=/tmp/upper,workdir=/tmp/work /tmp/merged
touch /tmp/merged/etc/resolv.conf
mkdir ~/bundle-w
cd ~/bundle-w
cp ~/oci-bundle/config.json .
ln -s /tmp/merged rootfs
# update config.json
# - add CAP_NET_RAW to the 5 capabilitiess
# - change hostID to 0 in uidMappings
# - change hostID to 0 in gidMappings
# - change noNewPrivileges to false (???)
crun --root=. --rootless=1 --log-level=debug run cnt1
```
Commands:

- crun --root=. --rootless=1 --log-level=debug run -b oci-bundle cnt1
- crun --root=. --rootless=1 --log-level=debug exec --cap=CAP_NET_RAW cnt1 /bin/sh


Gives:

```
/bin/sh: can't access tty; job control turned off
/ # 
/ # /bin/main 
cap_set_proc: Operation not permitted
Process ID: 3
User: root (UID: 0)
Group: root (GID: 0)
Capabilities: cap_net_raw=ep
socket: Operation not permitted
Failed to create a raw socket
```


### `CAP_NET_RAW` issue:

- <https://lwn.net/Articles/978846/>
- <https://man7.org/linux/man-pages/man7/user_namespaces.7.html>

### Work with NFS:

1. Extract with: `runqemu-extract-sdk tmp/deploy/images/qemuarm/image-sandbox-qemuarm.rootfs.tar.bz2 temp-nfs`
2. Run qemu with it: `runqemu qemuarm nographic ./temp-nfs`

## TODOs

- [x] Todo add `cgroup               /sys/fs/cgroup       cgroup2    defaults              0  0` to `/etc/fstab`
- [x] Custom linux which supports `erofs` or change erofs to other filesystem
- [ ] Find a way to not generate the qemuboot.conf file for the image-container-base image (makes no sense), otherwise call runqemu with parameters.

## Build CRUN script

```
#!/bin/bash
set -e

source /opt/lux/1.0/environment-setup-cortexa15t2hf-neon-oe-linux-gnueabi

mkdir -p m4
autoreconf -fi

mkdir -p build
cd build
../configure \
    --host=arm-oe-linux-gnueabi \
    --prefix=/usr \
    --disable-embedded-yajl \
    --enable-caps \
    --enable-seccomp \
    --disable-systemd

make -j$(nproc)
```

## Container

- Explain how to manipulate the base-image <meta-virtualization/classes/image-oci.bbclass>