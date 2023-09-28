package com.scanner.scansdk.di.modules

import androidx.camera.core.ImageCapture
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapper
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapperImpl
import org.koin.dsl.module
import org.koin.core.context.GlobalContext.loadKoinModules

val module = module {
    single { ImageCapture.Builder().build() }
    single <ImageCaptureWrapper> {ImageCaptureWrapperImpl(get())}
}
object ScanSdkCoreInitializer {
    fun initialize() {
        loadKoinModules(module)
    }
}