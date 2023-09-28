package com.scanner.scansdk.camera.processor.wrapper

import androidx.camera.core.ImageCapture
import java.util.concurrent.Executor

interface ImageCaptureWrapper {
    fun takePicture(
        outputOptions: ImageCapture.OutputFileOptions,
        executor: Executor,
        callback: ImageCapture.OnImageSavedCallback
    )
}