package com.dexatel.mobileotp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViberRegistrationTest {

    @get:Rule
    var rule = ScreenshotTakingRule()

    private lateinit var device: UiDevice

    @Before
    fun setup() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.pressHome()

        device.swipe(
            device.displayWidth / 2,
            device.displayHeight - 100,
            device.displayWidth / 2,
            0,
            5
        )

        device.wait(
            Until.hasObject(
                By.pkg("com.android.launcher3")
            ),
            5000
        )
    }

    @Test
    fun viberRegistrationTest() {
        val viberApp = device.wait(
            Until.findObject(
                By.desc("Viber")
            ), 5000
        )
        viberApp.click()

        val startNowButton = device.wait(
            Until.findObject(
                By.res("com.viber.voip:id/okBtn")
            ),
            10000
        )
        if (null != startNowButton) {
            startNowButton.click()
        } else {
            Assert.fail("Start now button is missing from the screen after launching the app")
        }

        device.wait(
            Until.findObject(
                By.res("com.google.android.gms:id/cancel")
            ),
            3000
        )?.click()

        val countryDropdown = device.wait(
            Until.findObject(
                By.res("com.viber.voip:id/registration_country_btn")
            ),
            3000
        )
        if (null != countryDropdown) {
            countryDropdown.click()
        } else {
            Assert.fail("Country selection dropdown is missing from 'Enter your phone number' screen")
        }

        val searchCountryField = device.wait(
            Until.findObject(
                By.res("com.viber.voip:id/search_src_text")
            ), 3000
        )
        searchCountryField.click()
        searchCountryField.text = "Armenia"

        val armenia = device.wait(
            Until.findObject(
                By.textContains("(+374)")
            ), 2000
        )
        if (null != armenia) {
            armenia.click()
        } else {
            Assert.fail("Armenia country doesn't appear in Search Country results")
        }

        val phoneNumberField = device.wait(
            Until.findObject(
                By.res("com.viber.voip:id/registration_phone_field")
            ),
            2000
        )

        if (null != phoneNumberField) {
            phoneNumberField.text = HelperFunctions.getRandomInvalidArmenianNumber()
        } else {
            Assert.fail("Phone number field is missing from 'Enter your phone number' screen")
        }

        val continueButton = device.wait(
            Until.findObject(
                By.res("com.viber.voip:id/btn_continue")
            ),
            1000
        )
        if (null != continueButton) {
            continueButton.click()
        } else {
            Assert.fail("Continue button is missing from 'Enter your phone number' screen")
        }

        device.wait(
            Until.findObject(
                By.res("com.viber.voip:id/yes_btn")
            ),
            3000
        )?.click()

        val permissionDenyButtonResId = "com.android.permissioncontroller:id/permission_deny_button"
        repeat(2) {
            val denyButton = device.wait(
                Until.findObject(
                    By.res(permissionDenyButtonResId)
                ),
                2000
            )
            if (null != denyButton) {
                denyButton.click()
                device.wait(
                    Until.gone(
                        By.res(permissionDenyButtonResId)
                    ),
                1000)
            }
        }

        val titleResourceId = "com.viber.voip:id/title"
        val flashCallScreenTitle = "You're almost there…"

        // Wait for the element's text to match either expected value
        device.wait(Until.findObject(By.res(titleResourceId)), 5000)
            .wait(Until.textMatches("Activate your account"), 2000)
        device.wait(Until.findObject(By.res(titleResourceId)), 5000)
            .wait(Until.textMatches(flashCallScreenTitle), 2000)

        val screenTitle = device.findObject(By.res(titleResourceId))

        if (screenTitle.text.equals(flashCallScreenTitle)) {
            val callMeButton = device.wait(
                Until.findObject(
                    By.res("com.viber.voip:id/call_me_button")
                ),
                2000
            )
            if (null != callMeButton) {
                callMeButton.click()
            } else {
                Assert.fail("Call me button is missing from send flash call screen")
            }

            val flashCallSentMessage = device.wait(
                Until.findObject(
                    By.textContains("Enter the last 4 digits of the phone number from the call you just received")
                ), 5000
            )
            Assert.assertNotEquals("Flash call isn't sent", null, flashCallSentMessage)
        } else {
            val codeSentMessage = device.wait(
                Until.findObject(
                    By.text("We're sending an SMS to phone number")
                ), 1000
            )
            Assert.assertNotEquals("OTP message isn't sent", null, codeSentMessage)
        }
    }
}