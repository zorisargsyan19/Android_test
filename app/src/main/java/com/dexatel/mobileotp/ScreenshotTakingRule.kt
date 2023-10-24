package com.dexatel.mobileotp

import android.content.ContentValues.TAG
import android.util.Log
import androidx.test.runner.screenshot.BasicScreenCaptureProcessor
import androidx.test.runner.screenshot.ScreenCaptureProcessor
import androidx.test.runner.screenshot.Screenshot
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import java.io.IOException

class ScreenshotTakingRule : TestWatcher() {

    override fun failed(e: Throwable?, description: Description?) {
        Log.d(TAG, "taking screenshot..." + description?.methodName)
        takeScreenshot(description?.methodName ?: "")
    }

    private fun takeScreenshot(screenShotName: String) {
        val screenCapture = Screenshot.capture()
        try {
            screenCapture.setName(screenShotName)
            val processorSet = HashSet<ScreenCaptureProcessor>()
            processorSet.add(BasicScreenCaptureProcessor())
            screenCapture.process(processorSet)
        } catch (ex: IOException) {
            Log.d(TAG, "error while taking screenshot " + ex.message)
        }
    }
}