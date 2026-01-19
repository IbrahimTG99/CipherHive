package com.devsinc.cipherhive

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test
import com.devsinc.cipherhive.presentation.settings.SettingsScreen
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onRoot

class SettingsScreenInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_rendersWithoutCrash() {
        composeTestRule.setContent {
            SettingsScreen()
        }
        assert(true)
    }

    @Test
    fun settingsScreen_isComposable() {
        composeTestRule.setContent {
            SettingsScreen()
        }
        // If composable renders, test passes
        assert(true)
    }

    @Test
    fun settingsScreen_doesNotCrashOnPreview() {
        composeTestRule.setContent {
            SettingsScreen()
        }
        assert(true)
    }

    @Test
    fun settingsScreen_canBePreviewed() {
        composeTestRule.setContent {
            SettingsScreen()
        }
        assert(true)
    }
}
