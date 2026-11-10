package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cl.duoc.tutorvirtual.ui.components.EsucarLogo
import cl.duoc.tutorvirtual.ui.theme.*
import cl.duoc.tutorvirtual.viewmodel.TutorState
import kotlinx.coroutines.delay

/** Entry screen: guests choose when to start; saved profiles receive a brief welcome. */
@Composable
internal fun WelcomeScreen(state: TutorState, onStart: () -> Unit) {
    val currentOnStart by rememberUpdatedState(onStart)
    LaunchedEffect(state.loaded, state.hasLocalProfile) {
        if (state.loaded && state.hasLocalProfile) {
            delay(2_000)
            currentOnStart()
        }
    }
    BoxWithConstraints(
        Modifier.fillMaxSize().testTag("welcome").background(
            Brush.verticalGradient(listOf(Color(0xFF003A19), CarabinerosVerde, Color(0xFF002510)))
        ), contentAlignment = Alignment.Center
    ) {
        val compact = maxHeight < 640.dp
        MountainBackdrop(Modifier.matchParentSize())
        Column(
            Modifier.widthIn(max = 460.dp).fillMaxSize().safeDrawingPadding()
                .verticalScroll(rememberScrollState()).padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(if (compact) 16.dp else 24.dp, Alignment.CenterVertically)
        ) {
            EsucarLogo(Modifier.size(if (compact) 120.dp else 144.dp, if (compact) 154.dp else 185.dp))
            Text("Tutor Virtual\nESUCAR", color = TarjetaBlanca,
                style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text("Trabajo de Título I, II y III", color = TarjetaBlanca,
                style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            HorizontalDivider(Modifier.width(48.dp), thickness = 3.dp, color = EsucarDorado)
            if (state.hasLocalProfile) {
                Text("Hola, ${state.profileName}", color = TarjetaBlanca,
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text("Bienvenido de vuelta", color = CarabinerosVerdeClaro,
                    style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
            } else {
                Text("Asistente académico para el desarrollo de tu anteproyecto de investigación",
                    color = TarjetaBlanca, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
            }
            if (!state.loaded || state.hasLocalProfile) {
                CircularProgressIndicator(Modifier.size(30.dp).testTag("welcome_loading"), color = EsucarDorado, strokeWidth = 3.dp)
                Text(if (state.loaded) "Preparando tu espacio…" else "Cargando tu perfil…",
                    color = CarabinerosVerdeClaro, style = MaterialTheme.typography.bodySmall)
            } else {
                Button(onClick = onStart, colors = ButtonDefaults.buttonColors(containerColor = EsucarDorado, contentColor = TextoPrincipal),
                    modifier = Modifier.fillMaxWidth().height(54.dp).testTag("begin")) { Text("Comenzar  →", fontWeight = FontWeight.Bold) }
                Text("Contenido disponible sin conexión", color = CarabinerosVerdeClaro, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/** Decorative, code-drawn landscape; no external image or network request is needed. */
@Composable
private fun MountainBackdrop(modifier: Modifier) {
    Canvas(modifier) {
        fun mountain(color: Color, points: List<Pair<Float, Float>>) {
            val path = Path().apply {
                moveTo(0f, size.height)
                points.forEach { (x, y) -> lineTo(x * size.width, y * size.height) }
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path, color)
        }
        mountain(Color(0xFF326846).copy(alpha = .45f), listOf(0f to .82f, .15f to .74f, .26f to .78f, .49f to .65f, .62f to .77f, .77f to .72f, 1f to .83f))
        mountain(Color(0xFF12482E), listOf(0f to .87f, .20f to .78f, .38f to .85f, .65f to .75f, .85f to .86f, 1f to .80f))
        mountain(Color(0xFF052F1C), listOf(0f to .93f, .22f to .88f, .43f to .94f, .74f to .85f, 1f to .92f))
    }
}
