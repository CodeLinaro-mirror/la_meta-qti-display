SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    '

RDEPENDS:packagegroup-qti-display = ' \
    libdrm \
    display-hal-linux \
    sdm-comp-linux \
    displaydlkm \
    ${@bb.utils.contains_any('VM_TARGET', 'art canoe', 'mmdrivers-hficore-dlkm', '', d)} \
    '
DEPENDS += " displaydevicetree"
DEPENDS += "${@bb.utils.contains_any('VM_TARGET', 'art canoe', 'hficore-devicetree', '', d)}"

