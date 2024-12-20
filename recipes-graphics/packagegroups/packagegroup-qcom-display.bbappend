RDEPENDS:${PN}:append = " \
    libdrm \
    wayland \
    wayland-protocols \
    weston \
    "

RDEPENDS:${PN}:append:qcom-custom-bsp = "\
    virtual/libgbm \
    "
