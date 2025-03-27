# Install

1. Install [repo](https://gerrit.googlesource.com/git-repo/)
3. Create a directory: `mkdir yocto-qemu-arm-playground && cd yocto-qemu-arm-playground`
3. Clone with: `repo init -u git@github.com:lucasdietrich/yocto-qemu-arm-playground.git -b main -m default.xml`
4. Sync with: `repo sync`
6. Install yocto project prerequisites: <https://docs.yoctoproject.org/ref-manual/system-requirements.html>
7. Source the environment: `source poky/oe-init-build-env`
8. Configure `local.conf`

```conf
MACHINE ?= "qemuarm"
#BB_NUMBER_THREADS = "14"
#PARALLEL_MAKE = "-j 14"
#PARALLEL_MAKEINST = "-j 14"
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
  /home/lucas/projects/yocto-qemu-arm/meta-playground \
  "
```

10. Build default image: `bitbake image-playground`
11. Run qemu: `runqemu qemuarm nographic`
12. Build the SDK: `bitbake -c populate_sdk image-playground`

## Tested machines

- `MACHINE = "qemuarm"`
- `MACHINE = "qemuarm-secureboot"`