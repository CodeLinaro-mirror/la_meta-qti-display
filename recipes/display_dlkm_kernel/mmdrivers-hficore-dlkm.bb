DESCRIPTION = "QTI HFI CORE DRIVER"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit linux-kernel-base deploy

PR = "r0"

DEPENDS = "rsync-native"
DEPENDS += "bc-native bison-native"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI     =  "file://display/vendor/qcom/opensource/mm-drivers/hfi_core/"

S = "${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/hfi_core"

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
	bbwarn "mmdrivers: Skipping do_configure.."
}

do_compile() {

    if [ ! -L "${KERNEL_PLATFORM_PATH}/vendor" ]; then
        ln -sf ${WORKSPACE}/vendor ${KERNEL_PLATFORM_PATH}/vendor
    fi
    if [ ! -L "${KERNEL_PLATFORM_PATH}/vendor/qcom/opensource/mm-drivers" ]; then
        ln -sf ${WORKSPACE}/display/vendor/qcom/opensource/mm-drivers  ${WORKSPACE}/vendor/qcom/opensource/mm-drivers
    fi

    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  && \

    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=../../display/vendor/qcom/opensource/mm-drivers/hfi_core \
    ENABLE_DDK_BUILD=${DDK_BUILD} \
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
    ROOTDIR=${WORKSPACE}/ \
    CONFIG_QTI_HFI_CORE=m \
    MODULE_OUT=${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/hfi_core \
    KERNEL_KIT=${KERNEL_OUT_PATH}/ \
    OUT_DIR=temp_out_dir \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh
}

do_strip_and_sign_modules() {
    install -m 0755 ${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/hfi_core/msm_hfi_core.ko -D ${WORKDIR}/msm_hfi_core.ko
    # strip debug symbols and sign the module
    ${STAGING_DIR_NATIVE}/usr/libexec/aarch64-oe-linux/gcc/aarch64-oe-linux/${STRIP_VERSION}/strip \
        --strip-debug ${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/hfi_core/msm_hfi_core.ko
    LD_LIBRARY_PATH=${LD_PATH} ${KERNEL_PREBUILT_PATH}/dist/sign-file sha1 ${KERNEL_PREBUILT_PATH}/dist/signing_key.pem \
        ${KERNEL_PREBUILT_PATH}/dist/signing_key.x509 ${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/hfi_core/msm_hfi_core.ko
}

do_install() {
    install -d ${D}${libdir}/modules/
    cp -rp ${WORKDIR}/display/vendor/qcom/opensource/mm-drivers/hfi_core/msm_hfi_core.ko ${D}${libdir}/modules/msm_hfi_core.ko
    chown 0:0 ${D}${libdir}/modules/msm_hfi_core.ko
}

do_deploy() {
	cp -rp ${WORKDIR}/msm_hfi_core.ko ${DEPLOYDIR}/
}

addtask do_deploy after do_install

python () {
    bb.build.addtask('do_strip_and_sign_modules', 'do_install', 'do_compile', d)
}

FILES:${PN} += "${libdir}/modules/*"

RM_WORK_EXCLUDE += "${PN}"

