package cl.duoc.tutorvirtual

import cl.duoc.tutorvirtual.data.model.*
import cl.duoc.tutorvirtual.data.repository.AcademicContent
import org.junit.Assert.*
import org.junit.Test

class DemoProfilesTest {
    @Test fun approvedWorkStopsRemindersButCorrectionsNeedStudentAction() {
        val approved = AcademicContent.hitos(mapOf("sent_0" to "true", "review_0" to "approved"))[0]
        val changes = AcademicContent.hitos(mapOf("sent_0" to "true", "review_0" to "changes"))[0]
        val submitted = AcademicContent.hitos(mapOf("sent_0" to "true"))[0]
        assertFalse(AcademicContent.needsStudentAction(approved))
        assertFalse(AcademicContent.needsStudentAction(submitted))
        assertTrue(AcademicContent.needsStudentAction(changes))
    }
    @Test fun demoStudentIsAssignedToDemoTeacher() {
        assertEquals(DemoProfiles.teacher.id, DemoProfiles.student.teacherId)
        assertTrue(DemoProfiles.canReview(DemoProfiles.teacher.id, DemoProfiles.student.id))
        assertFalse(DemoProfiles.canReview("otro-profesor", DemoProfiles.student.id))
    }
    @Test fun studentCannotChangeReviewOrIdentity() {
        assertTrue(DemoProfiles.canSaveField(DemoProfiles.student.id, "draft_0"))
        assertFalse(DemoProfiles.canSaveField(DemoProfiles.student.id, "review_0"))
        assertFalse(DemoProfiles.canSaveField(DemoProfiles.student.id, "sent_0"))
        assertFalse(DemoProfiles.canSaveField(DemoProfiles.student.id, "name"))
    }
    @Test fun teacherCanEditOwnPhotoButNotStudentDraft() {
        assertTrue(DemoProfiles.canSaveField(DemoProfiles.teacher.id, "photo"))
        assertFalse(DemoProfiles.canSaveField(DemoProfiles.teacher.id, "draft_0"))
        assertFalse(DemoProfiles.canEditAcademicWork(DemoProfiles.teacher.id))
    }
    @Test fun reviewRequiresAnActiveSubmissionToAffectStatus() {
        assertEquals(EstadoHito.POR_INICIAR, AcademicContent.hitos(mapOf("review_0" to "approved"))[0].estado)
        assertEquals(EstadoHito.APROBADO, AcademicContent.hitos(mapOf("sent_0" to "true", "review_0" to "approved"))[0].estado)
        assertEquals(EstadoHito.CON_OBSERVACIONES, AcademicContent.hitos(mapOf("sent_0" to "true", "review_0" to "changes"))[0].estado)
    }
}
