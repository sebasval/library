package com.scanner.scansdk.camera.processor
import androidx.appcompat.app.AppCompatActivity
import com.nhaarman.mockitokotlin2.*
import com.scanner.scansdk.ScanSdkPublicInterface
import com.scanner.scansdk.camera.processor.wrapper.ImageCaptureWrapper
import com.scanner.scansdk.rectangle.RectangleOverlay
import com.scanner.scansdkcore.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class PhotoProcessorTest {

    private fun provideTestActivity(): AppCompatActivity {
        val activityController = Robolectric.buildActivity(AppCompatActivity::class.java)
        val activity = activityController.get()
        activity.setTheme(R.style.AppTheme)
        activityController.create().resume()
        return activity
    }


    private fun provideMockImageCaptureWrapper():ImageCaptureWrapper{
        return mock()
    }
    private fun providePhotoProcessor(
        mockActivity: AppCompatActivity,
        mockScanSdk: ScanSdkPublicInterface,
        imageCaptureWrapper: ImageCaptureWrapper
    ): PhotoProcessor {
        return PhotoProcessor(mockActivity, mockScanSdk, imageCaptureWrapper)
    }

    private fun provideSpyRectangleOverlay(): RectangleOverlay {
        return RectangleOverlay(RuntimeEnvironment.getApplication())
    }



    @Test
    fun `test TakePhoto successful scenario`() {
        // Given
        val mockActivity = provideTestActivity()
        val mockScanSdk = mock<ScanSdkPublicInterface>()
        val imageCaptureWrapper = provideMockImageCaptureWrapper()
        val photoProcessor = providePhotoProcessor(mockActivity, mockScanSdk, imageCaptureWrapper)
        val mockRectangleOverlay = provideSpyRectangleOverlay()
        mockRectangleOverlay.corners = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)


        // When
        photoProcessor.takePhoto(mockRectangleOverlay)

        // Then
        verify(imageCaptureWrapper).takePicture(any(), any(), any())
    }
}
