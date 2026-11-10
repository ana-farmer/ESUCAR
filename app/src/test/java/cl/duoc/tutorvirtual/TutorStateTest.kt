package cl.duoc.tutorvirtual

import cl.duoc.tutorvirtual.viewmodel.TutorState
import cl.duoc.tutorvirtual.data.repository.AcademicContent
import cl.duoc.tutorvirtual.data.model.EstadoHito
import org.junit.Assert.assertEquals
import org.junit.Test

class TutorStateTest {
    @Test fun visitingBeforeDoesNotCreateARegisteredProfile() {
        assertEquals(false, TutorState(values = mapOf("welcomed" to "true")).hasLocalProfile)
        assertEquals(false, TutorState(values = mapOf("name" to "Camila")).hasLocalProfile)
    }
    @Test fun savedProfileProvidesTrimmedGreetingName() {
        val state = TutorState(values = mapOf("name" to " Camila ", "email" to "camila@example.com"))
        assertEquals(true, state.hasLocalProfile)
        assertEquals("Camila", state.profileName)
    }
    @Test fun submissionDoesNotGrantAcademicApproval() {
        val hitos = AcademicContent.hitos(mapOf("draft_0" to "Un problema", "sent_0" to "true"))
        assertEquals(EstadoHito.ENVIADO, hitos[0].estado)
        assertEquals(EstadoHito.POR_INICIAR, hitos[1].estado)
        assertEquals(EstadoHito.EN_DESARROLLO, hitos[3].estado)
        assertEquals(false, hitos.any { it.estado == EstadoHito.APROBADO })
    }
    @Test fun newStudentStartsWithoutArtificialProgress() {
        assertEquals(0f, TutorState().progress, 0.001f)
    }
    @Test fun partialAndFullCompletionUseEightResearchSections() {
        assertEquals(0.5f, TutorState(values = (0..3).associate { "done_$it" to "true" }).progress, 0.001f)
        assertEquals(1f, TutorState(values = (0..7).associate { "done_$it" to "true" }).progress, 0.001f)
    }
    @Test fun draftsAndUnrelatedStatesDoNotCountAsCompleted() {
        val state = TutorState(values = mapOf("draft_0" to "Un borrador", "done_0" to "false", "sent_1" to "true", "done_9" to "true"))
        assertEquals(0f, state.progress, 0.001f)
    }
}
