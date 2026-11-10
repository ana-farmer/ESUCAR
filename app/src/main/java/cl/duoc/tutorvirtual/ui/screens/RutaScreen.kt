package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tutorvirtual.R
import cl.duoc.tutorvirtual.data.model.ResearchJourney
import cl.duoc.tutorvirtual.data.repository.AcademicContent
import cl.duoc.tutorvirtual.ui.components.EsucarLogo
import cl.duoc.tutorvirtual.ui.theme.*
import cl.duoc.tutorvirtual.viewmodel.TutorState
import java.text.SimpleDateFormat
import java.util.Locale

internal val RouteForest = Color(0xFF063D29)
internal val RoutePaper = Color(0xFFF7F8EF)
private val RouteSoft = Color(0xFFDCE6CF)

@Composable
internal fun RutaScreen(state: TutorState, go: (String) -> Unit, onMenu: () -> Unit, onSwitch: () -> Unit) {
    val current = ResearchJourney.currentIndex(state.values)
    val stage = ResearchJourney.stages[current]
    val finished = ResearchJourney.stages.all { it.isComplete(state.values) }
    Column(Modifier.fillMaxSize().background(RoutePaper).verticalScroll(rememberScrollState()).testTag("dashboard")) {
        JourneyHeader(name = state.profileName.ifBlank { "estudiante" }, onMenu = onMenu, onDeadline = { go("avances") }, roleLabel = "Estudiante · Modo de prueba", onSwitch = onSwitch)
        Column(Modifier.widthIn(max = 720.dp).fillMaxWidth().align(Alignment.CenterHorizontally)
            .padding(horizontal = 16.dp).padding(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(shape = RoundedCornerShape(16.dp), color = RoutePaper, border = BorderStroke(1.dp, EsucarDorado)) {
                Column {
                    Box(Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(EsucarDorado, Color(0xFFE5D590))))
                        .padding(horizontal = 16.dp, vertical = 7.dp)) {
                        Text(if (finished) "TU ANTEPROYECTO ESTÁ PREPARADO" else "CONTINÚA DONDE QUEDASTE", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                    }
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Icon(painterResource(R.drawable.ic_research), contentDescription = null, tint = RouteForest, modifier = Modifier.size(56.dp))
                        Box(Modifier.width(1.dp).height(80.dp).background(EsucarDorado))
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                            Text(if (finished) "Revisa tu trabajo" else stage.action, fontFamily = FontFamily.Serif,
                                fontSize = 24.sp, lineHeight = 27.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                            Text("Etapa ${current + 1} de ${ResearchJourney.stages.size}", fontSize = 13.sp)
                            Button(onClick = { go("anteproyecto?section=${stage.nextSection(state.values)}") },
                                shape = RoundedCornerShape(10.dp), contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                                modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp).testTag("nav_anteproyecto")) {
                                Text(if (finished) "Revisar anteproyecto  →" else "Continuar anteproyecto  →", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().heightIn(min = 48.dp).clickable(role = Role.Button) { go("unidades") }.testTag("nav_unidades"),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Text("Tu ruta de titulación", fontFamily = FontFamily.Serif, fontSize = 23.sp,
                    fontWeight = FontWeight.Bold, color = RouteForest)
                HorizontalDivider(Modifier.weight(1f), color = EsucarDorado)
            }
            Column {
                ResearchJourney.stages.forEachIndexed { index, item ->
                    val complete = item.isComplete(state.values)
                    val active = index == current && !finished
                    Row(Modifier.fillMaxWidth().heightIn(min = 53.dp)
                        .clickable(role = Role.Button) { go("anteproyecto?section=${item.nextSection(state.values)}") }
                        .testTag("stage_$index"), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.width(44.dp).height(53.dp), contentAlignment = Alignment.Center) {
                            if (index > 0) Box(Modifier.align(Alignment.TopCenter).width(1.dp).height(27.dp).background(Color(0xFFA0A69C)))
                            if (index < ResearchJourney.stages.lastIndex) Box(Modifier.align(Alignment.BottomCenter).width(1.dp).height(27.dp).background(Color(0xFFA0A69C)))
                            if (active) Box(Modifier.size(38.dp).background(RouteSoft, CircleShape))
                            Box(Modifier.size(25.dp).background(if (complete) CarabinerosVerde else RoutePaper, CircleShape)
                                .border(if (active) 2.dp else 1.5.dp, if (complete || active) CarabinerosVerde else Color(0xFF7E847A), CircleShape), contentAlignment = Alignment.Center) {
                                if (complete) Text("✓", color = TarjetaBlanca, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                else if (active) Box(Modifier.size(15.dp).background(CarabinerosVerde, CircleShape))
                            }
                        }
                        Column(Modifier.weight(1f).clip(RoundedCornerShape(10.dp))
                            .background(if (active) RouteSoft else Color.Transparent).padding(horizontal = 10.dp, vertical = 6.dp)) {
                            Text(item.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextoPrincipal)
                            Text(if (complete) "Completado" else if (active) "En curso · Etapa ${index + 1} de 5" else "Pendiente · Etapa ${index + 1} de 5",
                                fontSize = 12.sp, lineHeight = 15.sp, color = Color(0xFF454C41))
                        }
                    }
                }
            }
            NextDeadline(state) { go("avances") }
        }
    }
}

@Composable
internal fun JourneyHeader(name: String, onMenu: () -> Unit, onDeadline: () -> Unit, roleLabel: String? = null, onSwitch: (() -> Unit)? = null) {
    Box(Modifier.fillMaxWidth().background(Brush.horizontalGradient(listOf(Color(0xFF003B26), Color(0xFF075539), RouteForest)))) {
        HeaderDecoration(Modifier.matchParentSize())
        Column(Modifier.widthIn(max = 720.dp).fillMaxWidth().align(Alignment.TopCenter)
            .statusBarsPadding().padding(horizontal = 22.dp).padding(top = 14.dp, bottom = 34.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(1f).clickable(onClick = onMenu).semantics { contentDescription = "Abrir menú ESUCAR" },
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    EsucarLogo(Modifier.size(46.dp, 60.dp))
                    Box(Modifier.width(2.dp).height(48.dp).background(EsucarDorado))
                    Column {
                        Text("ESUCAR", color = TarjetaBlanca, fontWeight = FontWeight.Bold, fontSize = 25.sp, letterSpacing = 1.sp)
                        Text("Tutor Virtual", color = TarjetaBlanca, fontSize = 14.sp)
                    }
                }
                IconButton(onClick = onMenu) { Icon(painterResource(R.drawable.ic_menu), "Abrir menú", tint = EsucarDorado) }
                IconButton(onClick = onDeadline) { Icon(painterResource(R.drawable.ic_bell), "Hitos y recordatorios", tint = TarjetaBlanca) }
            }
            Spacer(Modifier.height(14.dp))
            Text("Hola, $name", color = TarjetaBlanca, fontSize = 23.sp, lineHeight = 27.sp, fontWeight = FontWeight.SemiBold)
            Text(if (roleLabel?.startsWith("Profesor") == true) "Acompaña el avance de tus estudiantes" else "Tu investigación, paso a paso", color = TarjetaBlanca, fontSize = 14.sp)
            if (onSwitch != null) Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(roleLabel.orEmpty(), color = CarabinerosVerdeClaro, fontSize = 11.sp, modifier = Modifier.weight(1f))
                TextButton(onClick = onSwitch, modifier = Modifier.testTag("change_profile")) { Text("Cambiar de perfil", color = TarjetaBlanca, fontSize = 12.sp) }
            }
        }
    }
}

@Composable
private fun HeaderDecoration(modifier: Modifier) {
    Canvas(modifier) {
        val w = size.width; val h = size.height
        for (i in 0..5) {
            val x = w * .81f + i * w * .025f
            val y = h * .83f - i * h * .065f
            rotate(35f, Offset(x, y)) {
                drawOval(Color(0xFF83A65B).copy(alpha = .24f), Offset(x, y), Size(w * .028f, h * .08f))
            }
            rotate(-35f, Offset(x + w * .025f, y)) {
                drawOval(Color(0xFF83A65B).copy(alpha = .35f), Offset(x + w * .025f, y), Size(w * .025f, h * .075f))
            }
        }
        val gold = Path().apply {
            moveTo(0f, h - 9.dp.toPx()); cubicTo(w * .3f, h - 25.dp.toPx(), w * .7f, h - 5.dp.toPx(), w, h - 32.dp.toPx())
            lineTo(w, h); lineTo(0f, h); close()
        }
        drawPath(gold, EsucarDorado)
        val paper = Path().apply {
            moveTo(0f, h); cubicTo(0f, h - 34.dp.toPx(), w * .4f, h - 12.dp.toPx(), w * .7f, h - 13.dp.toPx())
            quadraticTo(w * .9f, h - 14.dp.toPx(), w, h - 18.dp.toPx()); lineTo(w, h); close()
        }
        drawPath(paper, RoutePaper)
    }
}

@Composable
private fun NextDeadline(state: TutorState, onClick: () -> Unit) {
    val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply { isLenient = false }
    val upcoming = AcademicContent.hitos(state.values).filter(AcademicContent::needsStudentAction)
        .mapNotNull { hito -> runCatching { parser.parse(hito.fechaLimite) }.getOrNull()?.let { hito to it } }.minByOrNull { it.second }
    Surface(onClick = onClick, modifier = Modifier.fillMaxWidth().testTag("nav_avances"), color = RouteSoft, shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(painterResource(R.drawable.ic_calendar), null, tint = RouteForest, modifier = Modifier.size(27.dp))
            Box(Modifier.width(1.dp).height(30.dp).background(EsucarDorado))
            Column(Modifier.weight(1f)) {
                Text("Próxima entrega", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(if (upcoming == null) "Agrega una fecha en Mis avances" else "${upcoming.first.titulo} · ${SimpleDateFormat("d MMM", Locale.forLanguageTag("es-CL")).format(upcoming.second)}",
                    fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
internal fun JourneyNavigation(route: String, isTeacher: Boolean = false, navigate: (String) -> Unit) {
    val active = if (isTeacher) { when (route) { "perfil" -> "perfil"; "revision", "revisiones" -> "revisiones"; else -> "inicio" } } else if (route == "tutor" || route == "biblioteca") route else "inicio"
    Surface(color = RoutePaper) {
        Column(Modifier.navigationBarsPadding()) {
            HorizontalDivider(color = Color(0xFFD5DDCC))
            Row(Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (if (isTeacher) listOf(Triple("inicio", "Estudiantes", R.drawable.ic_cap), Triple("revisiones", "Revisiones", R.drawable.ic_edit), Triple("perfil", "Mi perfil", R.drawable.ic_person)) else listOf(Triple("inicio", "Ruta", R.drawable.ic_flag), Triple("tutor", "Tutor", R.drawable.ic_cap), Triple("biblioteca", "Biblioteca", R.drawable.ic_books))).forEach { (destination, title, icon) ->
                    val selected = active == destination
                    Box(Modifier.weight(1f).heightIn(min = 60.dp).clip(RoundedCornerShape(10.dp))
                        .background(if (selected) RouteForest else Color.Transparent)
                        .selectable(selected, role = Role.Tab, onClick = { navigate(destination) }).testTag("nav_$destination")) {
                        Column(Modifier.fillMaxWidth().padding(vertical = 7.dp), horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)) {
                            Icon(painterResource(icon), null, tint = if (selected) TarjetaBlanca else TextoPrincipal, modifier = Modifier.size(25.dp))
                            Text(title, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, color = if (selected) TarjetaBlanca else TextoPrincipal)
                        }
                        if (selected) Box(Modifier.align(Alignment.BottomCenter).fillMaxWidth().height(3.dp).background(EsucarDorado))
                    }
                }
            }
        }
    }
}
