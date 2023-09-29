package com.scanner.scansdk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ExperimentalGetImage
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.scanner.scansdk.camera.binder.CameraBinder
import com.scanner.scansdk.camera.handler.CameraHandler
import com.scanner.scansdk.camera.processor.PhotoProcessor
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapper
import com.scanner.scansdk.rectangle.RectangleOverlay
import com.scanner.scansdkcore.databinding.ActivityCameraBinding
import org.koin.android.ext.android.inject
import org.opencv.android.OpenCVLoader
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
class CameraActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var rectangleOverlay: RectangleOverlay
    lateinit var photoProcessor: PhotoProcessor
    lateinit var cameraHandler: CameraHandler
    private val scanSdk: ScanSdkPublicInterface by inject()
    private val imageCaptureWrapper: ImageCaptureWrapper by inject()
    private val cameraBinder: CameraBinder by inject()

    private external fun findDocumentCorners(inputMatAddr: Long): FloatArray?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCvTag", "initOpenCv")
        }

        rectangleOverlay = viewBinding.rectangleOverlay
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraHandler = CameraHandler(
            this,
            viewBinding.viewFinder.surfaceProvider,
            cameraExecutor,
            findDocumentCorners = this::findDocumentCorners,
            rectangleOverlay,
            cameraBinder
        )
        photoProcessor = PhotoProcessor(this, scanSdk, imageCaptureWrapper, this::findDocumentCorners)

        if (allPermissionsGranted()) {
            cameraHandler.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        viewBinding.imageCaptureButton.setOnClickListener {
            photoProcessor.takePhoto()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraHandler.startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()

        init {
            System.loadLibrary("document_detector")
        }
    }
}