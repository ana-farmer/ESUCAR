package cl.duoc.tutorvirtual

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import cl.duoc.tutorvirtual.ui.screens.WelcomeScreen
import cl.duoc.tutorvirtual.ui.theme.TutorVirtualESUCARTheme
import cl.duoc.tutorvirtual.viewmodel.TutorState
import java.util.concurrent.atomic.AtomicBoolean
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {
    @get:Rule val rule = createComposeRule()

    @Test fun guestChoosesWhenToBegin() {
        val entered = AtomicBoolean(false)
        rule.setContent {
            TutorVirtualESUCARTheme(darkTheme = false) {
                WelcomeScreen(TutorState(loaded = true)) { entered.set(true) }
            }
        }
        rule.onNodeWithTag("begin").assertIsDisplayed()
        rule.onNodeWithTag("welcome_loading").assertDoesNotExist()
        assertFalse(entered.get())
        rule.onNodeWithTag("begin").performClick()
        assertTrue(entered.get())
    }

    @Test fun savedProfileReceivesGreetingAndAutomaticEntry() {
        val entered = AtomicBoolean(false)
        rule.setContent {
            TutorVirtualESUCARTheme(darkTheme = false) {
                WelcomeScreen(TutorState(loaded = true, values = mapOf("name" to "Camila", "email" to "camila@example.com"))) { entered.set(true) }
            }
        }
        rule.onNodeWithText("Hola, Camila").assertIsDisplayed()
        rule.onNodeWithText("Bienvenido de vuelta").assertIsDisplayed()
        rule.onNodeWithTag("begin").assertDoesNotExist()
        rule.onNodeWithTag("welcome_loading").assertExists()
        rule.waitUntil(6_000) { entered.get() }
    }
}
