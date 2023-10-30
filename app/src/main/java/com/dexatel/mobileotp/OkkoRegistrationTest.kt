package com.dexatel.mobileotp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.*
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class OkkoRegistrationTest {

    @get:Rule
    var rule = ScreenshotTakingRule()

    companion object {
        private lateinit var device: UiDevice

        @BeforeClass
        @JvmStatic
        fun setup() {
            device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            device.pressHome()
        }

//        @AfterClass
//        @JvmStatic
//        fun tearDown() {
//            device.pressRecentApps()
//            val multitasking = device.wait(Until.findObject(
//                By.res("com.google.android.apps.nexuslauncher:id/snapshot")), 5000)
//            if (null != multitasking) {
//                val width = device.displayWidth
//                val height = device.displayHeight / 2
//                device.swipe(width / 2, height, width, height / 2, 10)
//                val clearAllButton = device.wait(
//                    Until.findObject(
//                        By.res("com.google.android.apps.nexuslauncher:id/clear_all")
//                    ), 5000
//                )
//                if (null != clearAllButton) {
//                    clearAllButton.click()
//                    val isNotAppRunning = device.wait(
//                        Until.gone(
//                            By.res("com.google.android.apps.nexuslauncher:id/snapshot")
//                        ), 3000
//                    )
//                    Assert.assertTrue("The app still running", isNotAppRunning)
//                } else {
//                    Assert.fail("Couldn't force close the app")
//                }
//            } else {
//                Assert.fail("Multitasking bar isn't opened")
//            }
//        }
    }

    @Test
    fun registrationTest() {
        // Simulate the swipe-up gesture to open the app drawer
        device.swipe(
            device.displayWidth / 2,
            device.displayHeight - 100,
            device.displayWidth / 2,
            0,
            5
        )

        // Wait for the app drawer to open (assuming it's the default launcher)
        device.wait(Until.hasObject(By.pkg("com.android.launcher3")), 5000)

        // Find the Okko app and click on it
        val okkoApp = device.wait(
            Until.findObject(
                By.desc("Okko")
            ), 5000
        )
        okkoApp.click()

        // Find for user avatar displays
        val avatar = device.wait(
            Until.findObject(
                By.res("ru.more.play:id/menu_main_profile")
            ), 50000
        )

        // If permission request popup appears upon launch accept it
        device.findObject(
            By.res(
                "com.android.permissioncontroller:id/permission_allow_button"
            )
        )?.click()

        // Click on user avatar
        avatar.click()

        // Wait until Login button displays in Настройки menu and click on it
        val registerButton = device.wait(
            Until.findObject(
                By.res("ru.more.play:id/profileLoginButton")
            ), 2000
        )
        registerButton.click()

        // Input random phone number in the field
        val phoneField = device.wait(
            Until.findObject(
                By.res("ru.more.play:id/singleAuthorizationEditText")
            ), 5000
        )
        phoneField.text = "+37455443033"

        // Click on the Continue button
        val continueButton = device.wait(
            Until.findObject(
                By.res("ru.more.play:id/singleAuthorizationEnterButton")
            ), 2000
        )
        continueButton.click()

        // Wait until 'You are logging in by phone number' text displays on the screen
        val phoneRegisterInfo = device.wait(
            Until.findObject(
                By.textStartsWith("You are logging in by phone number")
            ), 5000
        )

        Assert.assertNotEquals("OTP message isn't sent", null, phoneRegisterInfo)
    }

    private fun getRandomPhoneNumber(): String {
        val numbers = "0123456789"
        val randomNumber = (1..6)
            .map { numbers.random() }
            .joinToString("")
        return "+37418$randomNumber"
    }
}