FILESEXTRAPATHS:prepend = "${WORKSPACE}/display/meta-qti-display/recipes/drm/files:"
FILESEXTRAPATHS:prepend = "${WORKSPACE}/display/layers/meta-qti-display/recipes/drm/files:"
SRC_URI = "git://git.codelinaro.org/clo/le/mesa/drm;protocol=git;nobranch=1;rev=6b4e956d299c6ff09a4abf89642ccc1beb455c88"
SRC_URI += "file://0001-meson-Add-new-strings-compatible-with-opensource.patch"


CFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
S = "${WORKDIR}/git"

FILES:${PN} += "${bindir}/*"

do_install:append() {
cp -rf ${S}/libdrm_macros.h ${D}${includedir}/libdrm/
}
do_configure[depends] += "virtual/kernel:do_shared_workdir"
