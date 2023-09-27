package com.scanner.scansdk

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

class ScanSdkCoreImplementation(private val context: Context) : ScanSdkPublicInterface {

    private var imageCapturedCallback: ((Bitmap) -> Unit)? = null
    override fun launchCamera(activity: AppCompatActivity) {
        val intent = Intent(context, CameraActivity::class.java)
        activity.startActivity(intent)
    }

    override fun setImageCapturedCallback(callback: (Bitmap) -> Unit) {
        imageCapturedCallback = callback
    }

    internal fun onImageCaptured(bitmap: Bitmap) {
        imageCapturedCallback?.invoke(bitmap)
    }
}
