FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:qcs610_odk_64 = " \
    file://0001-wayland-protocols-add-custom-position-support-in-xdg.patch \
"
