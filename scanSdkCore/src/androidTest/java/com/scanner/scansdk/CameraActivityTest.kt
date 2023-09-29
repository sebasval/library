package com.scanner.scansdk

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.camera.core.ImageCapture
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.scanner.scansdk.camera.binder.CameraBinder
import com.scanner.scansdk.camera.binder.CameraBinderImpl
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapper
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapperImpl
import com.scanner.scansdkcore.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.test.KoinTestRule
import org.koin.test.KoinTest
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.times

@RunWith(AndroidJUnit4::class)
class CameraActivityTest : KoinTest {

    private val application: Application = ApplicationProvider.getApplicationContext()

    private val module = module {
        single { ImageCapture.Builder().build() }
        single<ImageCaptureWrapper> { ImageCaptureWrapperImpl(get()) }
        single<CameraBinder> { CameraBinderImpl(get()) }
    }

    private val scanSdkLibraryModule = module {
        single<ScanSdkPublicInterface> { ScanSdkCoreImplementation(get()) }
    }

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        androidContext(application)
        modules(listOf(scanSdkLibraryModule, module))
    }

    @get:Rule
    var activityRule = ActivityScenarioRule(CameraActivity::class.java)

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    @Test
    fun checkStartCamera() {
        activityRule.scenario.onActivity { activity ->
            val cameraHandlerSpy = spy(activity.cameraHandler)
            activity.cameraHandler = cameraHandlerSpy

            activity.onRequestPermissionsResult(
                CameraActivity.REQUEST_CODE_PERMISSIONS,
                CameraActivity.REQUIRED_PERMISSIONS,
                IntArray(CameraActivity.REQUIRED_PERMISSIONS.size) { PackageManager.PERMISSION_GRANTED }
            )

            verify(cameraHandlerSpy, times(1)).startCamera()
        }
    }

    @Test
    fun checkCaptureButtonDisplayed() {
        onView(withId(R.id.image_capture_button)).perform(click())
    }
}
