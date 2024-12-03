DESCRIPTION = "QTI Display drivers"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit linux-kernel-base deploy

PR = "r0"

DEPENDS = "rsync-native"
DEPENDS += "bc-native bison-native"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/display-drivers/"
SRC_URI    +=  "file://start_display_le"
SRC_URI    +=  "file://display@.service"
SRC_URI    +=  "file://display.service"
SRC_URI    +=  "file://display_load.conf"

S = "${WORKDIR}/display/vendor/qcom/opensource/display-drivers"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"
GCCVER_AVAILABLE := "${@''.join(filter(lambda x: x != '%', '${GCCVERSION}'))}.0"
STRIP_VERSION = "${GCCVER_AVAILABLE}"

# Disable parallel make
PARALLEL_MAKE = ""

# Disable parallel make
PARALLEL_MAKE = "-j1"

LD_PATH = "${@oe.utils.conditional('KERNEL_TOOLS_USES_MUSLC', 'True', "${LD_PATH_MUSLC}", "${LD_PATH_GLIBC}", d)}"
do_compile[lockfiles] = "${TMPDIR}/build_modules.lock"

do_configure() {
	cp -f ${WORKSPACE}/display/vendor/qcom/opensource/display-drivers/Makefile.am ${WORKSPACE}/display/vendor/qcom/opensource/display-drivers/Makefile
}

do_compile() {

    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  && \

    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=../../display/vendor/qcom/opensource/display-drivers \
    ENABLE_DDK_BUILD=${DDK_BUILD} \
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
    ROOTDIR=${WORKSPACE}/ \
    MODULE_DRM_MSM=m \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/display-drivers \
    KERNEL_KIT=${KERNEL_OUT_PATH}/ \
    OUT_DIR=temp_out_dir \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh
}

do_strip_and_sign_modules() {
    install -m 0755 ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm_drm.ko -D ${WORKDIR}/msm_drm.ko
     # strip debug symbols and sign the module
    ${STAGING_DIR_NATIVE}/usr/libexec/aarch64-oe-linux/gcc/aarch64-oe-linux/${KP_STRIP_VERSION}/strip \
        --strip-debug ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm_drm.ko
    LD_LIBRARY_PATH=${LD_PATH} ${KERNEL_PREBUILT_PATH}/dist/sign-file sha1 ${KERNEL_PREBUILT_PATH}/dist/signing_key.pem \
        ${KERNEL_PREBUILT_PATH}/dist/signing_key.x509 ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm_drm.ko
}

do_install() {
      install -d ${D}${sysconfdir}/initscripts
      install -d ${D}/usr/include/display/
      install -m 755 ${WORKDIR}/start_display_le ${D}${sysconfdir}/initscripts
      install -d ${D}${libdir}/modules/

      cp -rp ${WORKDIR}/display/vendor/qcom/opensource/display-drivers/msm_drm.ko ${D}${libdir}/modules/msm_drm.ko
      chown 0:0 ${D}${libdir}/modules/msm_drm.ko

      mkdir -p ${S}/out/display/drm
      mkdir -p ${S}/out/display/hdcp
      mkdir -p ${S}/out/display/media

      python3 ${S}/display_kernel_headers.py --verbose --header_arch arm64 \
                     --gen_dir ${S}/out \
                     --display_include_uapi ${S}/include/uapi/display/drm/msm_drm_pp.h \
                                            ${S}/include/uapi/display/drm/sde_drm.h \
                                            ${S}/include/uapi/display/drm/msm_drm_aiqe.h \
                                            ${S}/include/uapi/display/hdcp/msm_hdmi_hdcp_mgr.h \
                                            ${S}/include/uapi/display/media/mmm_color_fmt.h \
                                            ${S}/include/uapi/display/media/msm_sde_rotator.h \
                     --unifdef ${STAGING_KERNEL_BUILDDIR}/scripts//unifdef --headers_install ${STAGING_KERNEL_BUILDDIR}/headers_install.sh

      cp -r ${S}/out/display/* ${D}/usr/include/display/
      install -m 0644 ${WORKDIR}/display@.service -D ${D}${systemd_unitdir}/system/display@.service
      install -m 0644 ${WORKDIR}/display.service -D ${D}${systemd_unitdir}/system/display.service
      install -m 0755 ${WORKDIR}/display_load.conf -D ${D}${sysconfdir}/modules-load.d/display_load.conf
}

do_deploy() {
	cp -rp ${WORKDIR}/msm_drm.ko ${DEPLOYDIR}/
}

addtask do_deploy after do_install

python () {
    bb.build.addtask('do_strip_and_sign_modules', 'do_install', 'do_compile', d)
}

FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "/etc/initscripts/start_display_le"
FILES:${PN} += "${systemd_unitdir}/system/display@.service"
FILES:${PN} += "${systemd_unitdir}/system/display.service"
FILES:${PN} += "${libdir}/modules/*"

RM_WORK_EXCLUDE += "${PN}"
