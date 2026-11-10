package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tutorvirtual.data.model.*
import cl.duoc.tutorvirtual.data.repository.AcademicContent
import cl.duoc.tutorvirtual.ui.theme.*
import cl.duoc.tutorvirtual.viewmodel.TutorState

@Composable
internal fun ProfileSwitcher(state: TutorState, dismiss: () -> Unit, select: (String) -> Unit) {
    AlertDialog(onDismissRequest = dismiss, containerColor = RoutePaper, tonalElevation = 0.dp,
        titleContentColor = RouteForest, textContentColor = TextoPrincipal,
        title = { Text("Cambiar de perfil", fontFamily = FontFamily.Serif) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Modo de prueba local. Estas dos cuentas están asociadas y comparten las entregas en este dispositivo.")
                DemoProfiles.all.forEach { profile ->
                    Surface(onClick = { select(profile.id); dismiss() }, shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (state.activeProfileId == profile.id) CarabinerosVerde else CarabinerosVerdeClaro),
                        color = if (state.activeProfileId == profile.id) CarabinerosVerdeClaro else RoutePaper,
                        modifier = Modifier.fillMaxWidth().testTag(if (profile.role == DemoRole.ESTUDIANTE) "select_student" else "select_teacher")) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(profile.name, fontWeight = FontWeight.Bold)
                            Text(if (profile.role == DemoRole.ESTUDIANTE) "Profesor guía: ${DemoProfiles.teacher.name}" else "Estudiante asociado: ${DemoProfiles.student.name}", fontSize = 12.sp)
                            if (state.activeProfileId == profile.id) Text("Perfil actual", color = CarabinerosVerde, fontSize = 12.sp)
                        }
                    }
                }
                Text("Guarda tus borradores antes de cambiar. No se requiere contraseña.", style = MaterialTheme.typography.bodySmall)
            }
        }, confirmButton = { TextButton(onClick = dismiss) { Text("Cerrar") } })
}

@Composable
internal fun DemoProfileBar(state: TutorState, switch: () -> Unit) {
    Surface(color = CarabinerosVerdeClaro) {
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(if (state.isTeacher) "Profesor · Modo de prueba" else "Estudiante · Modo de prueba", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(if (state.isTeacher) "Alumno: ${DemoProfiles.student.name}" else "Guía: ${DemoProfiles.teacher.name}", fontSize = 11.sp, lineHeight = 14.sp)
            }
            TextButton(onClick = switch, modifier = Modifier.testTag("change_profile")) { Text("Cambiar de perfil", fontSize = 12.sp) }
        }
    }
}

@Composable
private fun TeacherPage(content: @Composable ColumnScope.() -> Unit) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.widthIn(max = 720.dp).fillMaxWidth().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp), content = content)
    }
}

@Composable
private fun TeacherCard(content: @Composable ColumnScope.() -> Unit) {
    Surface(color = RoutePaper, shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, CarabinerosVerdeClaro)) {
        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp), content = content)
    }
}

@Composable
internal fun TeacherDashboard(state: TutorState, menu: () -> Unit, switch: () -> Unit, go: (String) -> Unit) {
    val hitos = AcademicContent.hitos(state.studentValues)
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).testTag("teacher_dashboard")) {
        JourneyHeader(name = state.profileName, onMenu = menu, onDeadline = { go("revisiones") },
            roleLabel = "Profesor · Modo de prueba", onSwitch = switch)
        Column(Modifier.widthIn(max = 720.dp).fillMaxWidth().align(Alignment.CenterHorizontally).padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("Tus estudiantes", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = RouteForest)
            TeacherCard {
                Text(DemoProfiles.student.name, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                Text("Asociado a ${DemoProfiles.teacher.name}", fontSize = 13.sp)
                Text(state.studentValues["course"].orEmpty())
                LinearProgressIndicator(progress = { state.studentProgress }, modifier = Modifier.fillMaxWidth(), color = CarabinerosVerde)
                Text("${(state.studentProgress * 100).toInt()}% de apartados completados por el estudiante", fontSize = 12.sp)
                Button(onClick = { go("revisiones") }, modifier = Modifier.fillMaxWidth().testTag("teacher_deliveries")) { Text("Ver entregas del estudiante") }
            }
            TeacherCard {
                Text("Pendientes de revisión: ${hitos.count { it.estado == EstadoHito.ENVIADO }}", fontWeight = FontWeight.Bold)
                Text("Aprobadas: ${hitos.count { it.estado == EstadoHito.APROBADO }}")
                Text("Con observaciones: ${hitos.count { it.estado == EstadoHito.CON_OBSERVACIONES }}")
                Text("Los porcentajes indican preparación del estudiante. Cada aprobación se registra por separado.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

internal fun hitoStatus(status: EstadoHito): String = when(status) {
    EstadoHito.POR_INICIAR -> "Por iniciar"
    EstadoHito.EN_DESARROLLO -> "En desarrollo"
    EstadoHito.ENVIADO -> "Enviado · pendiente de revisión"
    EstadoHito.CON_OBSERVACIONES -> "Correcciones solicitadas"
    EstadoHito.APROBADO -> "Aprobado por el profesor"
}

@Composable
internal fun TeacherDeliveries(state: TutorState, open: (Int) -> Unit) = TeacherPage {
    Text("Entregas de ${DemoProfiles.student.name}", fontFamily = FontFamily.Serif, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = RouteForest)
    Text("Profesor guía: ${DemoProfiles.teacher.name}")
    AcademicContent.hitos(state.studentValues).forEachIndexed { index, hito -> TeacherCard {
        Text("Hito ${hito.id}: ${hito.titulo}", fontWeight = FontWeight.Bold)
        Text(hitoStatus(hito.estado), color = CarabinerosVerde)
        if (state.studentValues["sent_$index"] == "true") {
            Text("Versión ${state.studentValues["submission_version_$index"] ?: "anterior"}", fontSize = 12.sp)
            Button(onClick = { open(index) }, modifier = Modifier.testTag("review_hito_$index")) { Text("Abrir entrega") }
        } else Text("El estudiante todavía no ha enviado este hito.", style = MaterialTheme.typography.bodySmall)
    } }
}

@Composable
internal fun TeacherReview(state: TutorState, index: Int, review: (Int, String, String, String) -> Unit) = TeacherPage {
    val hito = AcademicContent.hitos(state.studentValues)[index]
    val version = state.studentValues["submission_version_$index"].orEmpty()
    val snapshot = state.submission(index)
    var feedback by rememberSaveable(index, version) { mutableStateOf(state.studentValues["feedback_$index"].orEmpty()) }
    var invalid by rememberSaveable(index, version) { mutableStateOf(false) }
    Text(hito.titulo, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 25.sp, color = RouteForest)
    Text(DemoProfiles.student.name)
    Text(hitoStatus(hito.estado), color = CarabinerosVerde, modifier = Modifier.testTag("teacher_review_status"))
    if (state.studentValues["sent_$index"] != "true") {
        Text("El estudiante retiró esta entrega. Espera un nuevo envío.")
    } else if (snapshot.isEmpty() || version.isBlank()) {
        Text("Este envío anterior no tiene una copia registrada. El estudiante debe enviar una nueva versión para que puedas revisarla.")
    } else {
        Text("Versión enviada $version · copia guardada al enviar", style = MaterialTheme.typography.bodySmall)
        AcademicContent.hitoSections(index).forEach { section -> TeacherCard {
            Text(AcademicContent.sections[section], fontWeight = FontWeight.Bold)
            Text(snapshot[section].orEmpty())
        } }
        OutlinedTextField(feedback, { feedback = it; invalid = false }, label = { Text("Observaciones del profesor") },
            supportingText = { Text(if (invalid) "Escribe al menos 10 caracteres para solicitar correcciones." else "Obligatorias al solicitar correcciones.") },
            minLines = 3, isError = invalid, modifier = Modifier.fillMaxWidth().testTag("teacher_feedback"))
        Button(onClick = { review(index, "approved", feedback, version) }, modifier = Modifier.fillMaxWidth().testTag("approve_hito")) { Text("Aprobar entrega") }
        OutlinedButton(onClick = {
            invalid = feedback.trim().length < 10
            if (!invalid) review(index, "changes", feedback, version)
        }, modifier = Modifier.fillMaxWidth().testTag("request_changes")) { Text("Solicitar correcciones") }
    }
}
