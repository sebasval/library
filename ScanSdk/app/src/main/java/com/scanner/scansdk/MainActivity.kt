package com.scanner.scansdk

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.scanner.scansdk.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private val scanSdk: ScanSdkPublicInterface by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        scanSdk.launchCamera(this)
        scanSdk.setImageCapturedCallback {
            viewBinding.imageViewScanSdk.setImageBitmap(it)
        }
    }
}