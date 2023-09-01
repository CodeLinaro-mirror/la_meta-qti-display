SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-display \
    '

RDEPENDS:packagegroup-qti-display = ' \
    libdrm \
    display-hal-linux \
    gbm \
    wayland \
    wayland-protocols \
    weston \
    '

