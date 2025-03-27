# `local.conf`

```conf
MACHINE ?= "qemuarm"
#BB_NUMBER_THREADS = "14"
#PARALLEL_MAKE = "-j 14"
#PARALLEL_MAKEINST = "-j 14"
```

# `bblayers.conf`

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
  /home/lucas/projects/yocto-qemu-arm/meta-playground \
  "
```