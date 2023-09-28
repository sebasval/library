package com.scanner.scansdk.camera.binder

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider

class CameraBinderImpl(private val imageCapture: ImageCapture) : CameraBinder {
    override fun bindToLifecycle(
        cameraProvider: ProcessCameraProvider,
        activity: AppCompatActivity,
        cameraSelector: CameraSelector,
        preview: Preview,
        imageAnalysis: ImageAnalysis
    ) {
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                activity, cameraSelector, preview, imageCapture, imageAnalysis
            )

        } catch (exc: Exception) {
            Log.e("TAG", "Use case binding failed", exc)
        }
    }
}