package com.scanner.scansdk.di.modules

import androidx.camera.core.ImageCapture
import com.scanner.scansdk.camera.binder.CameraBinder
import com.scanner.scansdk.camera.binder.CameraBinderImpl
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapper
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapperImpl
import org.koin.dsl.module
import org.koin.core.context.GlobalContext.loadKoinModules

val module = module {
    single { ImageCapture.Builder().build() }
    single<ImageCaptureWrapper> { ImageCaptureWrapperImpl(get()) }
    single<CameraBinder> { CameraBinderImpl(get()) }
}
object ScanSdkCoreInitializer {
    fun initialize() {
        loadKoinModules(module)
    }
}