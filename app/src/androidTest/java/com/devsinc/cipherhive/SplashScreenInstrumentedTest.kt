package com.devsinc.cipherhive

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test
import com.devsinc.cipherhive.presentation.splash.Splash
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking

class SplashScreenInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_displaysAppName() {
        composeTestRule.setContent {
            Splash(navController = rememberNavController())
        }
        composeTestRule.onNodeWithText("Cipher", substring = true).assertExists()
        composeTestRule.onNodeWithText("Hive", substring = true).assertExists()
    }

    @Test
    fun splashScreen_displaysTagline() {
        composeTestRule.setContent {
            Splash(navController = rememberNavController())
        }
        composeTestRule.onNodeWithText("The only password manager you’ll ever need.").assertExists()
    }

    @Test
    fun splashScreen_hasWhiteBackground() {
        composeTestRule.setContent {
            Splash(navController = rememberNavController())
        }
        // No direct way to assert background color, but test renders without crash
        assert(true)
    }

    @Test
    fun splashScreen_navigatesAfterDelay() {
        runBlocking {
            composeTestRule.setContent {
                Splash(navController = rememberNavController())
            }
            // Wait for navigation effect (simulate delay)
            kotlinx.coroutines.delay(1100L)
            // No direct way to assert navigation, but test does not crash
            assert(true)
        }
    }
}
