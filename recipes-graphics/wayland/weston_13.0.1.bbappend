LICENSE_FLAGS = ""
LICENSE = "MIT"

FILESEXTRAPATHS:prepend := "${THISDIR}/weston-launch:"
FILESPATH =+ "${WORKSPACE}:"

SRC_URI:append:qcom = " file://weston.png \
              file://weston.desktop \
              file://xwayland.weston-start \
              file://systemd-notify.weston-start"

SRC_URI:append:qcom-custom-bsp = "   \
              file://0001-weston-Add-stack-protector-flag.patch"

SRC_URI:append:qcs610-odk-64 = "  \
              file://weston.ini \
              file://0001-Add-sdm-backend.patch \
              file://0001-weston-export-shared-headers.patch \
              file://0001-weston-add-protocol-extension-for-power-and-brightne.patch \
              file://0001-weston-add-surface-position-and-power-key.patch \
              file://0001-weston-add-support-color-calibration.patch \
	      file://0001-weston-Add-gbm_priv.h-file-for-downstream-utility.patch \
              "

SRC_URI:append:qcs9100 = "  file://0001-weston-add-sdm-option.patch \
                            file://0001-drm-backend-power-off-during-hotplug-disconnect.patch \
                            "

SRC_URI:append:qcs9100:qcom-base-bsp = " file://0001-weston-avoid-duplicate-format.patch"

SRC_URI:append:qcs8300 = "  file://0001-weston-add-sdm-option.patch \
                            file://0001-drm-backend-power-off-during-hotplug-disconnect.patch \
                            "

SRC_URI:append:qcs8300:qcom-base-bsp = " file://0001-weston-avoid-duplicate-format.patch"

SRC_URI:append:qcs615  = "  file://0001-weston-add-sdm-option.patch \
                            file://0001-weston-avoid-duplicate-format.patch \
                            file://0001-drm-backend-power-off-during-hotplug-disconnect.patch \
                            "

DEPENDS:append:qcom-custom-bsp = " property-vault qcom-libdmabufheap"
DEPENDS:append:qcs610-odk-64 = " property-vault qcom-display-hal-linux libgbm seatd"

EXTRA_OEMESON += "-Dbackend-default=auto -Dbackend-rdp=false"

RRECOMMENDS:${PN} = "weston-launch liberation-fonts"

REQUIRED_DISTRO_FEATURES:remove:qcs610-odk-64 = "opengl"

# select compositor, enable simple and demo clients and enable EGL
PACKAGECONFIG:qcs610-odk-64 = " \
                 egl \
                 clients \
                 shell-desktop \
                 screenshare \
                 shell-fullscreen \
                 shell-ivi \
                 image-jpeg \
                 "

PACKAGECONFIG:append:qcs610-odk-64 = "sdm disablepowerkey"
PACKAGECONFIG:append:qcs9100 = "kms"
PACKAGECONFIG:append:qcs8300 = "kms"
PACKAGECONFIG:append:qcs615  = "kms"

# Weston on SDM
PACKAGECONFIG[sdm] = "-Dbackend-sdm=true,-Dbackend-sdm=false"
# Weston with disabling display power key
PACKAGECONFIG[disablepowerkey] = "-Ddisable-power-key=true,-Ddisable-power-key=false"

LDFLAGS:append:qcs610-odk-64  = " -ldrmutils -ldisplaydebug -lglib-2.0 -ldmabufheap"

#meson script's CPP flags
CXXFLAGS:append:qcs610-odk-64  = " -I${STAGING_INCDIR}/sdm"
CXXFLAGS:append:qcs610-odk-64  = " -I${STAGING_INCDIR}/display/display"

do_install:append:qcs610-odk-64() {
    install -m 0644 ${WORKDIR}/weston.ini -D ${D}${sysconfdir}/xdg/weston/weston.ini
}

FILES:${PN} += "${bindir}/*"
FILES:${PN} += " ${libdir}/*.so"
FILES:${PN} += "${sysconfdir}/xdg/weston/weston.ini"
