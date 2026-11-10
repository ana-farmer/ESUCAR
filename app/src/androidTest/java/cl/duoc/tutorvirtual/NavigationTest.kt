package cl.duoc.tutorvirtual

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule val composeRule = createAndroidComposeRule<MainActivity>()
    @Before fun start() {
        lateinit var vm: cl.duoc.tutorvirtual.viewmodel.TutorViewModel
        composeRule.runOnUiThread { vm = androidx.lifecycle.ViewModelProvider(composeRule.activity)[cl.duoc.tutorvirtual.viewmodel.TutorViewModel::class.java] }
        composeRule.waitUntil(30000) { vm.state.value.loaded }
        composeRule.runOnUiThread { vm.switchProfile(cl.duoc.tutorvirtual.data.model.DemoProfiles.student.id) }
        composeRule.waitUntil(30000) { vm.state.value.activeProfileId == cl.duoc.tutorvirtual.data.model.DemoProfiles.student.id }
        composeRule.waitUntil(30000) {
            composeRule.onAllNodesWithTag("begin").fetchSemanticsNodes().isNotEmpty() ||
                composeRule.onAllNodesWithTag("nav_inicio").fetchSemanticsNodes().isNotEmpty()
        }
        if (composeRule.onAllNodesWithTag("begin").fetchSemanticsNodes().isNotEmpty())
            composeRule.onNodeWithTag("begin").performClick()
        composeRule.onNodeWithTag("nav_inicio").performClick()
        composeRule.waitUntil(30000) { composeRule.onAllNodesWithTag("dashboard").fetchSemanticsNodes().isNotEmpty() }
    }
    @Test fun guideUsesRepositoryAndOpensDetails() {
        composeRule.onNodeWithTag("nav_unidades").performClick()
        composeRule.onNodeWithText("Guía Metodológica ESUCAR").assertIsDisplayed()
        composeRule.onAllNodesWithText("Leer orientaciones →")[0].performClick()
        composeRule.onNodeWithText("1. Describir la situación a investigar.").assertIsDisplayed()
    }
    @Test fun projectDraftSurvivesActivityRecreation() {
        composeRule.onNodeWithTag("nav_anteproyecto").performClick()
        if (composeRule.onAllNodesWithText("Tu borrador").fetchSemanticsNodes().isEmpty())
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
    @Test fun journeyStageOpensItsOwnDraft() {
        composeRule.onNodeWithTag("stage_4").performScrollTo().performClick()
        composeRule.onNodeWithText("Resultados y conclusiones").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Tu borrador").assertExists()
    }
    @Test fun progressShowsAcademicMilestones() {
        composeRule.onNodeWithTag("nav_avances").performScrollTo().performClick()
        composeRule.onNodeWithText("Mis Hitos Académicos").assertIsDisplayed()
        composeRule.onNodeWithText("Hito 1: Definición del tema").assertIsDisplayed()
    }
}
