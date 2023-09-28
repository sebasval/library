package com.scanner.scansdk.camera.binder

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider

interface CameraBinder {
    fun bindToLifecycle(
        cameraProvider: ProcessCameraProvider,
        activity: AppCompatActivity,
        cameraSelector: CameraSelector,
        preview: Preview,
        imageAnalysis: ImageAnalysis
    )
}