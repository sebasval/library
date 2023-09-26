package com.scanner.scansdk

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

// ScanSdkCoreImplementation.kt
class ScanSdkCoreImplementation(private val context: Context) : ScanSdkPublicInterface {
    override fun launchCamera(activity: AppCompatActivity) {
        val intent = Intent(context, CameraActivity::class.java)
        activity.startActivity(intent)
    }

    override fun captureDocument(callback: (Uri?) -> Unit) {
        // Implementación para capturar el documento
    }
}
