package com.scanner.scansdk.camera.processor.wrapper

import androidx.camera.core.ImageCapture
import java.util.concurrent.Executor

class ImageCaptureWrapperImpl(private val imageCapture: ImageCapture) : ImageCaptureWrapper {
    override fun takePicture(
        outputOptions: ImageCapture.OutputFileOptions,
        executor: Executor,
        callback: ImageCapture.OnImageCapturedCallback
    ) {
        imageCapture.takePicture(executor, callback)
    }
}
