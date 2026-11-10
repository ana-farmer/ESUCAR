package cl.duoc.tutorvirtual

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import cl.duoc.tutorvirtual.data.model.DemoProfiles
import cl.duoc.tutorvirtual.viewmodel.TutorViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DemoProfilesFlowTest {
    @get:Rule val ui = createAndroidComposeRule<MainActivity>()
    private lateinit var vm: TutorViewModel
    @Before fun student() {
        ui.runOnUiThread { vm = ViewModelProvider(ui.activity)[TutorViewModel::class.java] }
        ui.waitUntil(15000) { vm.state.value.loaded }
        ui.runOnUiThread { vm.switchProfile(DemoProfiles.student.id) }
        ui.waitUntil(15000) { ui.onAllNodesWithTag("dashboard").fetchSemanticsNodes().isNotEmpty() }
    }
    private fun switch(teacher: Boolean) {
        ui.onNodeWithTag("change_profile").performClick()
        ui.onNodeWithTag(if (teacher) "select_teacher" else "select_student").performClick()
        ui.waitUntil(15000) {
            vm.state.value.isTeacher == teacher && ui.onAllNodesWithTag(if (teacher) "teacher_dashboard" else "dashboard").fetchSemanticsNodes().isNotEmpty()
        }
    }
    private fun openReview() {
        ui.onNodeWithTag("teacher_deliveries").performScrollTo().performClick()
        ui.onNodeWithTag("review_hito_0").performScrollTo().performClick()
    }
    private fun send() {
        val version = vm.state.value.value("submission_version_0").toIntOrNull() ?: 0
        ui.onNodeWithTag("send_hito_0").performScrollTo().performClick()
        ui.waitUntil(10000) { (vm.state.value.value("submission_version_0").toIntOrNull() ?: 0) > version }
    }
    @Test fun studentAndTeacherShareSubmissionFeedbackAndApproval() {
        ui.runOnUiThread { vm.save(mapOf(
            "draft_0" to "Analizar necesidades de seguridad de la comunidad en un sector delimitado.",
            "draft_1" to "El estudio se desarrollará en una comunidad durante un período definido."
        )) }
        ui.waitUntil(10000) { vm.state.value.value("draft_1").startsWith("El estudio") }
        ui.onNodeWithTag("nav_avances").performScrollTo().performClick()
        send()
        switch(true)
        openReview()
        ui.onNodeWithText("Analizar necesidades de seguridad de la comunidad en un sector delimitado.").assertExists()
        ui.onNodeWithTag("teacher_feedback").performScrollTo().performTextReplacement("Precisa el período de estudio y la población participante.")
        ui.onNodeWithTag("request_changes").performScrollTo().performClick()
        ui.waitUntil(10000) { vm.state.value.studentValues["review_0"] == "changes" }
        switch(false)
        ui.onNodeWithTag("nav_avances").performScrollTo().performClick()
        ui.onNodeWithTag("student_feedback_0").performScrollTo().assertTextContains("Precisa el período", substring = true)
        send()
        assertEquals(null, vm.state.value.studentValues["review_0"])
        switch(true)
        openReview()
        ui.onNodeWithTag("approve_hito").performScrollTo().performClick()
        ui.waitUntil(10000) { vm.state.value.studentValues["review_0"] == "approved" }
        switch(false)
        ui.onNodeWithTag("nav_avances").performScrollTo().performClick()
        ui.onNodeWithTag("student_status_0").performScrollTo().assertTextContains("Aprobado", substring = true)
    }
    @Test fun permissionsAndSubmissionSnapshotAreEnforced() {
        val draft = "Esta es la versión del problema enviada para revisión por el profesor."
        ui.runOnUiThread { vm.save(mapOf("draft_0" to draft, "draft_1" to "Delimitación del estudio con población, ubicación y período definidos.")) }
        ui.waitUntil(10000) { vm.state.value.value("draft_0") == draft }
        val old = vm.state.value.value("submission_version_0")
        ui.runOnUiThread { vm.submitHito(0) }
        ui.waitUntil(10000) { vm.state.value.value("submission_version_0") != old }
        val submittedVersion = vm.state.value.value("submission_version_0")
        ui.runOnUiThread { vm.save(mapOf("draft_0" to "Un borrador posterior que todavía no ha sido enviado al docente.")) }
        ui.waitUntil(10000) { vm.state.value.value("draft_0") != draft }
        assertEquals(draft, vm.state.value.submission(0)[0])
        ui.runOnUiThread { vm.reviewHito(0, "approved", "", submittedVersion) }
        ui.waitUntil(5000) { vm.state.value.error != null }
        assertNull(vm.state.value.studentValues["review_0"])
        switch(true)
        val studentDraft = vm.state.value.studentValues["draft_0"]
        ui.runOnUiThread { vm.save(mapOf("draft_0" to "Intento de edición desde profesor")) }
        ui.waitUntil(5000) { vm.state.value.error != null }
        assertEquals(studentDraft, vm.state.value.studentValues["draft_0"])
        ui.runOnUiThread { vm.reviewHito(0, "approved", "", "versión-anterior") }
        ui.waitUntil(5000) { vm.state.value.error?.contains("cambió") == true }
        assertNull(vm.state.value.studentValues["review_0"])
        switch(false)
    }
}
