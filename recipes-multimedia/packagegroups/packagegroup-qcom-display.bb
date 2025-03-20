SUMMARY = "QCOM Display package groups"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qcom-display \
    '

RDEPENDS:packagegroup-qcom-display = ' \
     mmdlkm \
     qcom-displaydlkm \
     qcom-displaydevicetree \
     qcom-display-hal-linux  \
     '

DEPENDS += " qcom-displaydevicetree"
