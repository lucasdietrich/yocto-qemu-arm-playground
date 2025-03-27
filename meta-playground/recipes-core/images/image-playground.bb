require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "Playground image"

IMAGE_ROOTFS_SIZE ?= "8192"

IMAGE_INSTALL += "packagegroup-core-full-cmdline \
                  openssh \
                  strace \
                  htop \
                  "
