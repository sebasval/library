package com.scanner.scansdk

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import strikt.api.expectThat
import strikt.assertions.isEqualTo


@RunWith(RobolectricTestRunner::class)
class ScanSdkCoreImplementationTest {

    private fun provideApplicationContext(): Context {
        return ApplicationProvider.getApplicationContext()
    }

    private fun provideMockBitmap(): Bitmap {
        return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    }

    private fun provideScanSdkCoreImplementation(
        mockContext: Context
    ): ScanSdkCoreImplementation {
        return ScanSdkCoreImplementation(mockContext)
    }

    @Test
    fun `test LaunchCamera`() {
        // Given
        val mockContext = provideApplicationContext()
        val mockActivityController = Robolectric.buildActivity(AppCompatActivity::class.java).create()
        val mockActivity = mockActivityController.get()
        val scanSdkCoreImplementation = provideScanSdkCoreImplementation(mockContext)

        // When
        scanSdkCoreImplementation.launchCamera(mockActivity)

        // Then
        val expectedIntent = Intent(mockContext, CameraActivity::class.java)
        val actualIntent = shadowOf(mockActivity).nextStartedActivity
        expectThat(actualIntent.component).isEqualTo(expectedIntent.component)

    }

    @Test
    fun `test OnImageCaptured`() {
        // Given
        val mockContext = provideApplicationContext()
        val mockBitmap = provideMockBitmap()
        val scanSdkCoreImplementation = provideScanSdkCoreImplementation(mockContext)

        var callbackInvoked = false
        scanSdkCoreImplementation.setImageCapturedCallback {
            callbackInvoked = true
        }

        // When
        scanSdkCoreImplementation.onImageCaptured(mockBitmap)

        // Then
        expectThat(callbackInvoked).isEqualTo(true)
    }
}
