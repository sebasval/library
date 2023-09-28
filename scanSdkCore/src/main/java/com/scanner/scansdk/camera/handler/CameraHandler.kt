package com.scanner.scansdk.camera.handler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.Preview.SurfaceProvider
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.scanner.scansdk.camera.binder.CameraBinder
import com.scanner.scansdk.rectangle.RectangleOverlay
import org.opencv.android.Utils
import org.opencv.core.Mat
import java.io.ByteArrayOutputStream
import java.util.concurrent.ExecutorService

class CameraHandler(
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
                val bitmap = imageProxyToBitmap(imageProxy)
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

    private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val yBuffer = image.planes[0].buffer
        val uBuffer = image.planes[1].buffer
        val vBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val uSize = uBuffer.remaining()
        val vSize = vBuffer.remaining()

        val nv21 = ByteArray(ySize + uSize + vSize)


        yBuffer.get(nv21, 0, ySize)
        vBuffer.get(nv21, ySize, vSize)
        uBuffer.get(nv21, ySize + vSize, uSize)

        val yuvImage =
            android.graphics.YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

}
