SUMMARY = "QCOM Display package groups"

PACKAGE_ARCH = "${SOC_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"

PROVIDES = "${PACKAGES}"

PACKAGES = "${PN}"

RDEPENDS:${PN} = " \
    kernel-module-displaydlkm \
    displaydevicetree \
    libcec \
    "

RDEPENDS:${PN}:append:qcm6490 += " \
display-hal-linux \
"