package com.scanner.scansdk.camera.handler

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.scanner.scansdk.camera.binder.CameraBinder
import com.scanner.scansdk.camera.utils.toBitmapAnalyser
import com.scanner.scansdk.rectangle.RectangleOverlay
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.util.concurrent.ExecutorService

open class CameraHandler(
    private val activity: AppCompatActivity,
    private val surfaceProvider: SurfaceProvider,
    private val cameraExecutor: ExecutorService,
    private val findDocumentCorners: (Long) -> FloatArray?,
    val rectangleOverlay: RectangleOverlay,
    private val cameraBinder: CameraBinder
) {

    @ExperimentalGetImage
    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity)

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
            imageProxy.image?.let {
                val bitmap = imageProxy.toBitmapAnalyser()
                val mat = Mat()
                Utils.bitmapToMat(bitmap, mat)
                val cornersFound = findDocumentCorners(mat.nativeObjAddr)

                if (cornersFound != null) {
                    rectangleOverlay.setImageDimensions(imageProxy.width, imageProxy.height)
                    rectangleOverlay.corners = cornersFound
                    rectangleOverlay.invalidate()
                }
            }
            imageProxy.close()
        }

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraBinder.bindToLifecycle(cameraProvider,activity,cameraSelector,preview,imageAnalysis)

        }, ContextCompat.getMainExecutor(activity))
    }
}
