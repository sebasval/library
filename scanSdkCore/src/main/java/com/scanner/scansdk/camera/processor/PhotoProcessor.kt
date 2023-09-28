package com.scanner.scansdk.camera.processor

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Matrix
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import com.scanner.scansdk.ScanSdkPublicInterface
import com.scanner.scansdk.camera.ImageCaptureManager
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapper
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapperImpl
import com.scanner.scansdk.rectangle.RectangleOverlay
import java.text.SimpleDateFormat
import java.util.Locale

class PhotoProcessor(
    private val activity: AppCompatActivity,
    private val scanSdk: ScanSdkPublicInterface,
    private var imageCaptureWrapper: ImageCaptureWrapper,
) {

    private val TAG = "PhotoProcessor"
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    fun takePhoto(rectangleOverlay: RectangleOverlay) {
        val corners = rectangleOverlay.corners ?: return
        val (left, top, width, height) = getDimensions(corners)
        val outputOptions = createOutputOptions()
        captureImage(outputOptions, left, top, width, height)
    }

    private fun getDimensions(corners: FloatArray): Dimensions {
        val x1 = (corners[0]).toInt()
        val y1 = (corners[1]).toInt()
        val x2 = (corners[2]).toInt()
        val y2 = (corners[3]).toInt()
        val x3 = (corners[4]).toInt()
        val y3 = (corners[5]).toInt()
        val x4 = (corners[6]).toInt()
        val y4 = (corners[7]).toInt()

        val left = minOf(x1, x2, x3, x4)
        val top = minOf(y1, y2, y3, y4)
        val right = maxOf(x1, x2, x3, x4)
        val width = right - left
        val bottom = maxOf(y1, y2, y3, y4)
        val height = bottom - top

        return Dimensions(left, top, width, height)
    }

    private fun createOutputOptions(): ImageCapture.OutputFileOptions {
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }

        return ImageCapture.OutputFileOptions.Builder(
            activity.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
    }

    private fun captureImage(
        outputOptions: ImageCapture.OutputFileOptions,
        left: Int, top: Int, width: Int, height: Int
    ) {
        imageCaptureWrapper.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(activity),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = output.savedUri ?: return
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(activity.contentResolver, savedUri)
                    val rotatedBitmap = rotateBitmap(bitmap, 90.0F)
                    val croppedBitmap = cropBitmap(rotatedBitmap, left, top, width, height)
                    processCapturedImage(croppedBitmap)
                }
            }
        )
    }

    private fun processCapturedImage(bitmap: Bitmap?) {
        bitmap?.let {
            scanSdk.onImageCaptured(it)
            activity.finish()
        } ?: Toast.makeText(
            activity.applicationContext,
            "Toma la foto de nuevo, imagen no reconocida",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun cropBitmap(bitmap: Bitmap, left: Int, top: Int, width: Int, height: Int): Bitmap? {
        return Bitmap.createBitmap(
            bitmap,
            left,
            top,
            width,
            height
        )
    }

    private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private data class Dimensions(val left: Int, val top: Int, val width: Int, val height: Int)
}
