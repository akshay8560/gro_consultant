package com.clinicapp.models

data  class CameraCaptureState (
    val position: CameraPositions,
    val isPreview: Boolean,
    val path: String? = null,
)