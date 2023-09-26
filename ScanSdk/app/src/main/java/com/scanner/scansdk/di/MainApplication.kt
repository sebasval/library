package com.scanner.scansdk.di

import android.app.Application
import com.scanner.scansdklibrary.di.ScanSdkLibraryInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@MainApplication)
            ScanSdkLibraryInitializer.initialize()
        }
    }
}