
RDEPENDS:packagegroup-qcom-display += " \
        libdrm \
	gbm \
        wayland \
        wayland-protocols \
        weston \
	"

RDEPENDS:${PN}:append:qcom = " \
    libdrm \
    wayland \
    wayland-protocols \
    weston \
    "

RDEPENDS:${PN}:append:qcom-custom-bsp = "\
    virtual/libgbm \
    "

RDEPENDS:${PN}:remove:qcm6490-idp:qcom-base-bsp = "\
libdrm \
weston \
wayland \
wayland-protocols \
"

RDEPENDS:${PN}:remove:qcs6490-rb3gen2-core-kit:qcom-base-bsp = "\
libdrm \
weston \
wayland \
wayland-protocols \
"

RDEPENDS:${PN}:remove:qcs6490-rb3gen2-vision-kit:qcom-base-bsp = "\
libdrm \
weston \
wayland \
wayland-protocols \
"

RDEPENDS:${PN}:remove:qcs6490-rb3gen2-industrial-kit:qcom-base-bsp = "\
libdrm \
weston \
wayland \
wayland-protocols \
"
