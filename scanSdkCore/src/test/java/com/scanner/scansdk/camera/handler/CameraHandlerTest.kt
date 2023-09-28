package com.scanner.scansdk.camera.handler

import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Preview.SurfaceProvider
import com.nhaarman.mockitokotlin2.*
import com.scanner.scansdk.camera.binder.CameraBinder
import com.scanner.scansdk.rectangle.RectangleOverlay
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.ExecutorService
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import com.scanner.scansdkcore.R
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.Executors

@RunWith(RobolectricTestRunner::class)
class CameraHandlerTest {

    private fun provideTestActivity(): AppCompatActivity {
        val activityController = Robolectric.buildActivity(AppCompatActivity::class.java)
        val activity = activityController.get()
        activity.setTheme(R.style.AppTheme)
        activityController.create().resume()
        return activity
    }

    private fun provideMockFindDocumentCorners(): (Long) -> FloatArray? = mock {
        onGeneric { invoke(any()) } doReturn floatArrayOf(1.0f, 1.0f)
    }

    private fun provideSpyRectangleOverlay(): RectangleOverlay {
        return RectangleOverlay(RuntimeEnvironment.getApplication())
    }

    private fun provideCameraBinder():CameraBinder{
        return mock()
    }

    private fun providesCameraHandler(
        mockActivity: AppCompatActivity,
        surfaceProvider: SurfaceProvider,
        mockExecutorService: ExecutorService,
        mockFindDocumentCorners: (Long) -> FloatArray?,
        mockRectangleOverlay: RectangleOverlay,

    ): CameraHandler {
        return CameraHandler(
            mockActivity,
            surfaceProvider,
            mockExecutorService,
            mockFindDocumentCorners,
            mockRectangleOverlay,
            provideCameraBinder()
        )
    }

    @Test
    fun `test StartCamera`() {
        // Given
        val activity = provideTestActivity()
        val mockSurfaceProvider: SurfaceProvider = mock()
        val executorService = Executors.newSingleThreadExecutor()
        val findDocumentCorners = provideMockFindDocumentCorners()
        val rectangleOverlay = provideSpyRectangleOverlay()

        val cameraHandler = providesCameraHandler(
            activity,
            mockSurfaceProvider,
            executorService,
            findDocumentCorners,
            rectangleOverlay
        )

        cameraHandler.startCamera()
    }
}
