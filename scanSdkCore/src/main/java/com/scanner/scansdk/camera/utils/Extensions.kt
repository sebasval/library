package com.scanner.scansdk.camera.utils

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import java.io.ByteArrayOutputStream
import kotlin.math.pow
import kotlin.math.sqrt

fun ImageProxy.toBitmapAnalyser(): Bitmap {
    val yBuffer = planes[0].buffer
    val uBuffer = planes[1].buffer
    val vBuffer = planes[2].buffer

    val ySize = yBuffer.remaining()
    val uSize = uBuffer.remaining()
    val vSize = vBuffer.remaining()

    val nv21 = ByteArray(ySize + uSize + vSize)

    yBuffer.get(nv21, 0, ySize)
    vBuffer.get(nv21, ySize, vSize)
    uBuffer.get(nv21, ySize + vSize, uSize)

    val yuvImage = android.graphics.YuvImage(
        nv21,
        ImageFormat.NV21,
        this.width,
        this.height,
        null
    )
    val out = ByteArrayOutputStream()
    yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 100, out)
    val imageBytes = out.toByteArray()
    return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
}

data class Point(val x: Float, val y: Float)
data class PointDistance(val dist: Float, val point: Point)

fun FloatArray.sortCorners(): List<PointDistance> {
    val sorted = mutableListOf<PointDistance>()
    val point = Point(0F, 0F)

    for (i in indices step 2) {
        val x = this[i]
        val y = this[i + 1]
        val dist = (point.x - x).pow(2) + (point.y - y).pow(2)
        sorted.add(PointDistance(sqrt(dist), Point(x, y)))
    }

    sorted.sortBy { it.dist }

    return sorted
}

fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}




