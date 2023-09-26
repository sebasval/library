package com.scanner.scansdk

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

interface ScanSdkPublicInterface {
    fun launchCamera(activity: AppCompatActivity)
    fun captureDocument(callback: (Uri?) -> Unit)
}
