SUMMARY = "QTI Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qcom-display \
    '

RDEPENDS:packagegroup-qcom-display = ' \
    libdrm \
    libdrm-tests \
    display-hal-linux \
    gbm \
    wayland \
    wayland-protocols \
    weston \
    kernel-module-displaydlkm \
    '

