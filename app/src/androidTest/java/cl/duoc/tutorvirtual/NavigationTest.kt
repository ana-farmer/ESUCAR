package cl.duoc.tutorvirtual

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    @Before fun start() {
        composeRule.waitUntil(10000) {
            composeRule.onAllNodesWithText("Comenzar  →").fetchSemanticsNodes().isNotEmpty() ||
                composeRule.onAllNodesWithText("Bienvenido al Tutor Virtual").fetchSemanticsNodes().isNotEmpty()
        }
        if (composeRule.onAllNodesWithText("Comenzar  →").fetchSemanticsNodes().isNotEmpty())
            composeRule.onNodeWithText("Comenzar  →").performClick()
    }
    @Test fun guideUsesRepositoryAndOpensDetails() {
        composeRule.onNodeWithTag("nav_unidades").performClick()
        composeRule.onNodeWithText("Guía Metodológica ESUCAR").assertIsDisplayed()
        composeRule.onAllNodesWithText("Leer orientaciones →")[0].performClick()
        composeRule.onNodeWithText("1. Describir la situación a investigar.").assertIsDisplayed()
    }
    @Test fun projectDraftSurvivesActivityRecreation() {
        composeRule.onNodeWithTag("nav_anteproyecto").performClick()
        composeRule.onAllNodesWithText("Problema de investigación")[0].performScrollTo().performClick()
        composeRule.onNodeWithText("Tu borrador").performTextReplacement("Analizar las necesidades de seguridad de una comunidad delimitada.")
        composeRule.onNodeWithText("Completar").performScrollTo().performClick()
        composeRule.waitUntil(5000) {
            composeRule.onAllNodesWithText("Analizar las necesidades de seguridad de una comunidad delimitada.").fetchSemanticsNodes().size >= 2
        }
        composeRule.activityRule.scenario.recreate()
        composeRule.waitForIdle()
        composeRule.onAllNodesWithText("Analizar las necesidades de seguridad de una comunidad delimitada.")[0].assertExists()
    }
    @Test fun progressShowsAcademicMilestones() {
        composeRule.onNodeWithTag("nav_avances").performClick()
        composeRule.onNodeWithText("Mis Hitos Académicos").assertIsDisplayed()
        composeRule.onNodeWithText("Hito 1: Definición del tema").assertIsDisplayed()
    }
}
