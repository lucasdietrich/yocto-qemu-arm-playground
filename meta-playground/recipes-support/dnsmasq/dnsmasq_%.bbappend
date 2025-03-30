FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://dnsmasq.conf"

do_install:append() {
    # Copy the dnsmasq configuration file to the appropriate location
    install -d ${D}${sysconfdir}/dnsmasq.d
    install -m 644 ${UNPACKDIR}/dnsmasq.conf ${D}${sysconfdir}/
}