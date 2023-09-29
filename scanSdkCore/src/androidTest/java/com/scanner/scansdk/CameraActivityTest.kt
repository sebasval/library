package com.scanner.scansdk

import android.Manifest
import android.app.Instrumentation
import android.content.pm.PackageManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.scanner.scansdkcore.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CameraActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(CameraActivity::class.java)

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    @Test
    fun checkCaptureButtonDisplayed() {
        onView(withId(R.id.image_capture_button)).perform(click())
    }
}
