package com.scanner.scansdk.camera.handler

import androidx.appcompat.app.AppCompatActivity
import com.nhaarman.mockitokotlin2.*
import com.scanner.scansdk.camera.ImageCaptureManager
import com.scanner.scansdk.rectangle.RectangleOverlay
import com.scanner.scansdkcore.databinding.ActivityCameraBinding
import org.junit.Test
import org.junit.runner.RunWith
import strikt.api.expectThat
import strikt.assertions.isNotNull
import java.util.concurrent.ExecutorService
import org.mockito.Mockito.spy
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CameraHandlerTest {

    private fun provideMockActivity(): AppCompatActivity {
        val realActivity = Robolectric.setupActivity(AppCompatActivity::class.java)
        return spy(realActivity)
    }

    private fun provideMockViewBinding(): ActivityCameraBinding = mock()

    private fun provideMockExecutorService(): ExecutorService = mock()

    private fun provideMockFindDocumentCorners(): (Long) -> FloatArray? = mock {
        onGeneric { invoke(any()) } doReturn floatArrayOf(1.0f, 1.0f)
    }

    private fun provideMockRectangleOverlay(): RectangleOverlay = mock {
        on { setImageDimensions(any(), any()) } doReturn Unit
        on { invalidate() } doReturn Unit
    }

    private fun providesCameraHandler(
        mockActivity: AppCompatActivity,
        mockViewBinding: ActivityCameraBinding,
        mockExecutorService: ExecutorService,
        mockFindDocumentCorners: (Long) -> FloatArray?,
        mockRectangleOverlay: RectangleOverlay
    ): CameraHandler {
        return CameraHandler(
            mockActivity,
            mockViewBinding,
            mockExecutorService,
            mockFindDocumentCorners,
            mockRectangleOverlay
        )
    }


    @Test
    fun `test StartCamera`() {
        // Given
        val mockActivity = provideMockActivity()
        val mockViewBinding = provideMockViewBinding()
        val mockExecutorService = provideMockExecutorService()
        val mockFindDocumentCorners = provideMockFindDocumentCorners()
        val mockRectangleOverlay = provideMockRectangleOverlay()

        val cameraHandler = providesCameraHandler(
            mockActivity,
            mockViewBinding,
            mockExecutorService,
            mockFindDocumentCorners,
            mockRectangleOverlay
        )

        // When
        cameraHandler.startCamera()

        // Then
        verify(mockRectangleOverlay).setImageDimensions(any(), any())
        verify(mockRectangleOverlay, times(1)).setImageDimensions(any(), any())
        expectThat(cameraHandler.imageCapture).isNotNull()
        expectThat(ImageCaptureManager.imageCapture).isNotNull()
    }
}
