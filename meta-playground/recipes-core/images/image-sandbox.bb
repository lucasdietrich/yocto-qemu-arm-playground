require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "Sandbox image"

IMAGE_ROOTFS_SIZE ?= "8192"

IMAGE_INSTALL += "packagegroup-core-full-cmdline \
                  dnsmasq \
                  ca-certificates \
                  openssl \
                  openssh \
                  optee-examples \
                  libcap \
                  libcap-bin \
                  crun \
                  libcgroup \
                  containers-runtime \
                  "

# debug tools
IMAGE_INSTALL += "htop \
                  strace \
                  socat \
                  nano \
                  jq \
                  tree \
                  valgrind \
                  iperf3 \
                  tcpdump \
                  uftrace \
                  perf \
                  systemtap kernel-dev kernel-devsrc kernel-modules \
                  "

EXTRA_IMAGE_FEATURES = "debug-tweaks tools-profile dbg-pkgs"

TOOLCHAIN_HOST_TASK += "nativesdk-umoci \
                        nativesdk-skopeo \
                        nativesdk-erofs-utils \
                        nativesdk-systemtap \
                        "

rootfs_fstab_cgroup2() {
    # Path /etc/fstab and add cgroup2 mount
    echo "cgroup               /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${IMAGE_ROOTFS}/etc/fstab
}
ROOTFS_POSTPROCESS_COMMAND += "rootfs_fstab_cgroup2; "