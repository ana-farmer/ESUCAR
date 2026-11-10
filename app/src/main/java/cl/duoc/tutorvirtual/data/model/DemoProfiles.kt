package cl.duoc.tutorvirtual.data.model

enum class DemoRole { ESTUDIANTE, PROFESOR }

data class DemoProfile(val id: String, val name: String, val email: String, val role: DemoRole, val teacherId: String? = null)

object DemoProfiles {
    val teacher = DemoProfile("profesor-prueba-1", "Profesor de prueba 1", "profesor1@esucar.example", DemoRole.PROFESOR)
    val student = DemoProfile("estudiante-prueba-1", "Estudiante de prueba 1", "estudiante1@esucar.example", DemoRole.ESTUDIANTE, teacher.id)
    val all = listOf(student, teacher)
    fun find(id: String?) = all.firstOrNull { it.id == id }
    fun canReview(teacherId: String?, studentId: String) =
        find(teacherId)?.role == DemoRole.PROFESOR && find(studentId)?.teacherId == teacherId
    fun canEditAcademicWork(profileId: String?) = profileId == student.id
    fun canSaveField(profileId: String?, field: String): Boolean {
        if (find(profileId) == null) return false
        if (field in setOf("photo", "course")) return true
        return canEditAcademicWork(profileId) &&
            (field.matches(Regex("(draft|done)_[0-7]")) || field.matches(Regex("date_[0-3]")))
    }
}
