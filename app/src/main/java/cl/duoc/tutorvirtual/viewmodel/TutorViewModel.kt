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

private val android.content.Context.store by preferencesDataStore("esucar")

data class TutorState(
    val loaded: Boolean = false,
    val values: Map<String, String> = emptyMap(),
    val error: String? = null
) {
    fun value(key: String) = values[key].orEmpty()
    val progress: Float get() = (0 until 6).count { value("done_$it") == "true" } / 6f
}

class TutorViewModel(application: Application) : AndroidViewModel(application) {
    private val mutable = MutableStateFlow(TutorState())
    val state = mutable.asStateFlow()
    private val key = stringPreferencesKey("student")
    init {
        viewModelScope.launch {
            try {
                application.store.data.collect { prefs ->
                    val json = JSONObject(prefs[key] ?: "{}")
                    mutable.value = TutorState(true, json.keys().asSequence().associateWith { json.getString(it) })
                }
            } catch (e: IOException) {
                mutable.value = mutable.value.copy(loaded = true, error = "No se pudieron leer los datos locales.")
            }
        }
    }
    fun save(values: Map<String, String>) {
        viewModelScope.launch {
            try {
                getApplication<Application>().store.edit { prefs ->
                    val json = JSONObject(prefs[key] ?: "{}")
                    values.forEach { (k, v) -> json.put(k, v) }
                    prefs[key] = json.toString()
                }
            } catch (e: IOException) {
                mutable.value = mutable.value.copy(error = "No se pudo guardar. Intenta nuevamente.")
            }
        }
    }
}
