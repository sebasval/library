package com.scanner.scansdklibrary.di

import com.scanner.scansdk.ScanSdkCoreImplementation
import com.scanner.scansdk.ScanSdkPublicInterface
import com.scanner.scansdk.di.modules.ScanSdkCoreInitializer
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module


val scanSdkCoreModule = module {
    single<ScanSdkPublicInterface> { ScanSdkCoreImplementation(get()) }
}
object ScanSdkLibraryInitializer {
    fun initialize() {
        loadKoinModules(scanSdkCoreModule)
        ScanSdkCoreInitializer.initialize()
    }
}