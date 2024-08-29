RDEPENDS:${PN}:append = " \
    libdrm \
    libdrm-tests \
    wayland \
    wayland-protocols \
    weston \
    "

RDEPENDS:${PN}:append:qcom-custom-bsp = "\
    gbm \
    "