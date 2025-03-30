require recipes-extended/images/container-base.bb

# tar output is not required
OCI_IMAGE_TAR_OUTPUT = "false"

DESCRIPTION = "Container base image for the sandbox"

CONTAINER_SHELL = "busybox"

IMAGE_INSTALL += " \
    os-release \
    busybox \
    libgcc \
    libcap \
"

# debug tools
IMAGE_INSTALL += " \
    libcap-bin \
    strace \
    uftrace \
    perf \
    tcpdump \
"

OCI_IMAGE_AUTHOR = "Lucas"
OCI_IMAGE_ENV_VARS = "\
    LUX_CONTAINER=1 \
"

DEPENDS += "umoci-native \
            skopeo-native \
            erofs-utils-native \
            "

rootfs_add_empty_resolv_conf() {
    install -d ${IMAGE_ROOTFS}/etc
    touch ${IMAGE_ROOTFS}/etc/resolv.conf
}
ROOTFS_POSTPROCESS_COMMAND += "rootfs_add_empty_resolv_conf; "

# Custom task to generate the OCI bundle .img from OCI image
do_image_erofs_bundle[depends] += "umoci-native:do_populate_sysroot erofs-utils-native:do_populate_sysroot"
do_image_erofs_bundle[dirs] = "${WORKDIR}"
fakeroot do_image_erofs_bundle() {
    cd ${IMGDEPLOYDIR}
    # clear previous image
	rm -rf ${IMAGE_NAME}.img

    # Unpack OCI image as OCI bundle into WORKDIR
    umoci unpack --rootless --image "${IMAGE_NAME}${IMAGE_NAME_SUFFIX}-oci:${OCI_IMAGE_TAG}" "${WORKDIR}/oci-bundle"

    # Create the EROFS image from the OCI bundle
    mkfs.erofs -zlz4hc --all-root "${IMAGE_NAME}.img" "${WORKDIR}/oci-bundle"

    # Remove the temporary OCI bundle
    rm -rf "${WORKDIR}/oci-bundle"

    # Create a convenient symlink to the image
    ln -sf "${IMAGE_NAME}.img" "${IMAGE_BASENAME}.img"
}

# Ensure the image gets built after the normal image is done
addtask do_image_erofs_bundle before do_image_complete after do_image_oci