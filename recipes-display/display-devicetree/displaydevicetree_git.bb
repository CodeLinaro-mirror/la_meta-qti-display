DESCRIPTION = "QTI Display devicetree"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit module deploy
FILESPATH   =+ "${WORKSPACE}/display/vendor/qcom/opensource:"
SRC_URI     =  "file://display-devicetree/"
S = "${WORKDIR}/display-devicetree"

DTC := "${KBUILD_OUTPUT}/scripts/dtc/dtc"
KERNEL_INCLUDE := "${STAGING_KERNEL_DIR}/include/"
EXTRA_OEMAKE += "DTC='${DTC}' KERNEL_INCLUDE='${KERNEL_INCLUDE}'"

do_install() {
   :
}

do_compile() {
    oe_runmake ${EXTRA_OEMAKE} qcm6490-display
    oe_runmake ${EXTRA_OEMAKE} qcm6490-display-rb3
}

do_deploy() {
    install -d ${DEPLOYDIR}/tech_dtbs
    install -m 0644 \
    ${S}/display/*.dtbo \
    ${DEPLOYDIR}/tech_dtbs/
}
addtask do_deploy after do_install
RM_WORK_EXCLUDE += "${PN}"
