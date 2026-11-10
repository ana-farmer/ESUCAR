package cl.duoc.tutorvirtual.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import cl.duoc.tutorvirtual.data.model.*
import cl.duoc.tutorvirtual.data.repository.AcademicContent

private val android.content.Context.store by preferencesDataStore("esucar")

data class TutorState(
    val loaded: Boolean = false,
    val values: Map<String, String> = emptyMap(),
    val error: String? = null,
    val activeProfileId: String? = null,
    val studentValues: Map<String, String> = values
) {
    fun value(key: String) = values[key].orEmpty()
    val profileName: String get() = value("name").trim()
    val hasLocalProfile: Boolean get() = profileName.isNotBlank() && value("email").isNotBlank()
    val isTeacher: Boolean get() = activeProfileId == DemoProfiles.teacher.id
    val demoMode: Boolean get() = DemoProfiles.find(activeProfileId) != null
    val progress: Float get() = completion(values)
    val studentProgress: Float get() = completion(studentValues)
    private fun completion(data: Map<String, String>) =
        (0 until ResearchJourney.sectionCount).count { data["done_$it"] == "true" } / ResearchJourney.sectionCount.toFloat()
    fun submission(index: Int): Map<Int, String> = runCatching {
        val json = JSONObject(studentValues["submission_$index"] ?: "{}")
        json.keys().asSequence().mapNotNull { key -> key.toIntOrNull()?.let { it to json.getString(key) } }.toMap()
    }.getOrDefault(emptyMap())
}

class TutorViewModel(application: Application) : AndroidViewModel(application) {
    private val mutable = MutableStateFlow(TutorState())
    val state = mutable.asStateFlow()
    private val legacyKey = stringPreferencesKey("student")
    private val studentKey = stringPreferencesKey("demo_student_v1")
    private val teacherKey = stringPreferencesKey("demo_teacher_v1")
    private val activeKey = stringPreferencesKey("demo_active_profile_v1")
    private fun decode(text: String?) = JSONObject(text ?: "{}")
    private fun map(json: JSONObject) = json.keys().asSequence().associateWith { json.optString(it) }
    private fun identify(json: JSONObject, profile: DemoProfile): JSONObject = json.apply {
        put("name", profile.name); put("email", profile.email)
        if (optString("course").isBlank()) put("course", "Trabajo de Título I")
    }
    private fun fail(message: String) { mutable.value = mutable.value.copy(error = message) }
    init {
        viewModelScope.launch {
            try {
                application.store.edit { prefs ->
                    // Keep the original personal record intact; copy its drafts only once.
                    if (prefs[studentKey] == null) prefs[studentKey] = identify(decode(prefs[legacyKey]), DemoProfiles.student).toString()
                    if (prefs[teacherKey] == null) prefs[teacherKey] = identify(JSONObject(), DemoProfiles.teacher).toString()
                    if (DemoProfiles.find(prefs[activeKey]) == null) prefs[activeKey] = DemoProfiles.student.id
                }
                application.store.data.collect { prefs ->
                    val active = DemoProfiles.find(prefs[activeKey]) ?: DemoProfiles.student
                    val student = map(identify(decode(prefs[studentKey]), DemoProfiles.student))
                    val values = if (active.role == DemoRole.PROFESOR) map(identify(decode(prefs[teacherKey]), active)) else student
                    mutable.value = TutorState(true, values, activeProfileId = active.id, studentValues = student)
                }
            } catch (e: IOException) {
                mutable.value = mutable.value.copy(loaded = true, error = "No se pudieron leer los datos locales.")
            }
        }
    }
    fun switchProfile(id: String) {
        if (DemoProfiles.find(id) == null) { fail("Perfil de prueba desconocido."); return }
        viewModelScope.launch {
            try { getApplication<Application>().store.edit { it[activeKey] = id } }
            catch (e: IOException) { fail("No se pudo cambiar de perfil. Intenta nuevamente.") }
        }
    }
    fun save(values: Map<String, String>) {
        val actor = mutable.value.activeProfileId
        if (!mutable.value.loaded || !values.keys.all { DemoProfiles.canSaveField(actor, it) }) {
            fail("Este perfil no puede modificar esos datos."); return
        }
        val target = if (actor == DemoProfiles.teacher.id) teacherKey else studentKey
        viewModelScope.launch {
            try {
                getApplication<Application>().store.edit { prefs ->
                    val json = decode(prefs[target])
                    values.forEach { (key, value) -> json.put(key, value) }
                    prefs[target] = json.toString()
                }
            } catch (e: IOException) { fail("No se pudo guardar. Intenta nuevamente.") }
        }
    }
    fun submitHito(index: Int) {
        if (index !in 0..3 || !DemoProfiles.canEditAcademicWork(mutable.value.activeProfileId)) {
            fail("Solo el estudiante asociado puede enviar su trabajo."); return
        }
        viewModelScope.launch {
            try {
                getApplication<Application>().store.edit { prefs ->
                    val json = decode(prefs[studentKey])
                    val sections = AcademicContent.hitoSections(index)
                    if (!sections.all { json.optString("draft_$it").trim().length >= 20 }) {
                        fail("Guarda al menos 20 caracteres en cada apartado de este hito antes de enviarlo."); return@edit
                    }
                    val snapshot = JSONObject()
                    sections.forEach { snapshot.put(it.toString(), json.optString("draft_$it")) }
                    json.put("submission_$index", snapshot.toString())
                    json.put("submission_version_$index", ((json.optString("submission_version_$index").toIntOrNull() ?: 0) + 1).toString())
                    json.put("submitted_at_$index", System.currentTimeMillis().toString())
                    json.put("sent_$index", "true")
                    listOf("review", "feedback", "reviewer", "reviewed_version").forEach { json.remove("${it}_$index") }
                    prefs[studentKey] = json.toString()
                }
            } catch (e: IOException) { fail("No se pudo enviar el hito. Intenta nuevamente.") }
        }
    }
    fun withdrawHito(index: Int) {
        if (index !in 0..3 || !DemoProfiles.canEditAcademicWork(mutable.value.activeProfileId)) return
        viewModelScope.launch {
            try {
                getApplication<Application>().store.edit { prefs ->
                    val json = decode(prefs[studentKey])
                    if (json.optString("review_$index").isNotBlank()) { fail("La entrega ya fue revisada. Puedes enviar una nueva versión."); return@edit }
                    json.put("sent_$index", "false")
                    prefs[studentKey] = json.toString()
                }
            } catch (e: IOException) { fail("No se pudo retirar el envío.") }
        }
    }
    fun reviewHito(index: Int, decision: String, feedback: String, expectedVersion: String) {
        val actor = mutable.value.activeProfileId
        if (index !in 0..3 || !DemoProfiles.canReview(actor, DemoProfiles.student.id) || decision !in setOf("approved", "changes")) {
            fail("Este perfil no puede revisar la entrega."); return
        }
        if (decision == "changes" && feedback.trim().length < 10) { fail("Describe las correcciones solicitadas con al menos 10 caracteres."); return }
        viewModelScope.launch {
            try {
                getApplication<Application>().store.edit { prefs ->
                    val json = decode(prefs[studentKey])
                    if (json.optString("sent_$index") != "true" || expectedVersion.isBlank() || json.optString("submission_version_$index") != expectedVersion) {
                        fail("La entrega cambió. Ábrela nuevamente antes de revisarla."); return@edit
                    }
                    json.put("review_$index", decision)
                    json.put("feedback_$index", feedback.trim())
                    json.put("reviewer_$index", actor)
                    json.put("reviewed_version_$index", expectedVersion)
                    prefs[studentKey] = json.toString()
                }
            } catch (e: IOException) { fail("No se pudo guardar la revisión.") }
        }
    }
}
