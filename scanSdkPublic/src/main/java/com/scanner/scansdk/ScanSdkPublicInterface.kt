package com.scanner.scansdk

import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

interface ScanSdkPublicInterface {
    fun launchCamera(activity: AppCompatActivity)
    fun setImageCapturedCallback(callback: (Bitmap) -> Unit)
}
