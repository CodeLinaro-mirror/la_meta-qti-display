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
    ${@bb.utils.contains("MACHINE_FEATURES", "qti-vm", "", "mmdlkm", d)} \
    displaydlkm \
    gbm \
    ${@bb.utils.contains("DISTRO_FEATURES", "wayland", "weston", "", d)} \
    libcec \
    '

RDEPENDS:packagegroup-qti-display:vienna = ' displaydevicetree'
