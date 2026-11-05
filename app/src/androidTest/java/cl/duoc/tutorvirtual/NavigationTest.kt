package cl.duoc.tutorvirtual

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun guiaOpensAndReturnsHome() {
        composeRule.onNodeWithText("Guía metodológica").performClick()
        composeRule.onNodeWithText("Guía Metodológica ESUCAR").assertIsDisplayed()
        composeRule.onNodeWithText("Volver al inicio").performClick()
        composeRule.onNodeWithText("Bienvenido al Tutor Virtual").assertIsDisplayed()
    }

    @Test
    fun avancesOpensHitos() {
        composeRule.onNodeWithText("Mis avances").performClick()
        composeRule.onNodeWithText("Mis Hitos Académicos").assertIsDisplayed()
        composeRule.onNodeWithText("Volver al inicio").performClick()
        composeRule.onNodeWithText("Bienvenido al Tutor Virtual").assertIsDisplayed()
    }
}
