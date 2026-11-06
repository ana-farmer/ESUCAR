package cl.duoc.tutorvirtual.ui.screens

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import cl.duoc.tutorvirtual.R
import cl.duoc.tutorvirtual.ui.components.EsucarLogo
import cl.duoc.tutorvirtual.ui.utils.rememberWindowSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import cl.duoc.tutorvirtual.data.model.EstadoHito
import cl.duoc.tutorvirtual.data.repository.AcademicContent
import cl.duoc.tutorvirtual.data.repository.GuiaRepository
import cl.duoc.tutorvirtual.ui.theme.*
import cl.duoc.tutorvirtual.viewmodel.*
import java.io.File
import kotlinx.coroutines.launch

private val destinations = listOf("inicio" to "Inicio", "unidades" to "Unidades", "anteproyecto" to "Anteproyecto", "avances" to "Avances")
private val symbols = listOf(R.drawable.ic_home, R.drawable.ic_book, R.drawable.ic_edit, R.drawable.ic_chart)

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun EsucarApp(vm: TutorViewModel = viewModel()) {
    val state by vm.state.collectAsState()
    val nav = rememberNavController()
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: "bienvenida"
    fun go(r: String) { nav.navigate(r) { launchSingleTop = true } }
    if (!state.loaded) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }; return }
    val startRoute = remember { if (state.value("welcomed") == "true") "inicio" else "bienvenida" }
    ModalNavigationDrawer(
        drawerState = drawer,
        gesturesEnabled = route != "bienvenida",
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = FondoSuperficie) {
                Text("Tutor Virtual ESUCAR", style = MaterialTheme.typography.titleLarge, color = CarabinerosVerde, fontWeight = FontWeight.Bold, modifier = Modifier.padding(24.dp))
                (destinations + listOf("tutor" to "Tutor Virtual", "biblioteca" to "Biblioteca", "perfil" to "Mi perfil")).forEach { (destination, label) ->
                    NavigationDrawerItem(label = { Text(label) }, selected = route == destination,
                        onClick = { go(destination); scope.launch { drawer.close() } },
                        colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = CarabinerosVerdeClaro, selectedTextColor = CarabinerosVerde))
                }
            }
        }
    ) {
    Scaffold(
        containerColor = if (route == "bienvenida") CarabinerosVerde else FondoSuperficie,
        topBar = {
            if (route != "bienvenida") TopAppBar(
                title = { Column {
                    Text(when(route) { "inicio" -> "Tutor Virtual ESUCAR"; "unidades" -> "Mis Unidades"; "anteproyecto" -> "Mi Anteproyecto"; "avances" -> "Mis Avances"; "tutor" -> "Tutor Virtual"; "biblioteca" -> "Biblioteca"; "perfil" -> "Mi Perfil"; else -> "Guía metodológica" }, fontWeight = FontWeight.Bold)
                    Text("Trabajo de Título · ESUCAR", style = MaterialTheme.typography.labelMedium, color = CarabinerosVerdeClaro)
                } },
                navigationIcon = { if (route == "inicio") IconButton(onClick = { scope.launch { drawer.open() } }) { Icon(painterResource(R.drawable.ic_menu), "Abrir menú", tint = TarjetaBlanca) } else TextButton(onClick = { if (!nav.popBackStack()) go("inicio") }, modifier = Modifier.semantics { contentDescription = "Volver a la pantalla anterior" }) { Text("‹", color = TarjetaBlanca, style = MaterialTheme.typography.headlineMedium) } },
                actions = { IconButton(onClick = { go("avances") }) { Icon(painterResource(R.drawable.ic_bell), "Hitos y recordatorios", tint = TarjetaBlanca) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CarabinerosVerde, titleContentColor = TarjetaBlanca)
            )
        },
        bottomBar = {
            if (route != "bienvenida") NavigationBar(containerColor = TarjetaBlanca) {
                destinations.forEachIndexed { i, (r, title) -> NavigationBarItem(
                    modifier = Modifier.testTag("nav_$r"),
                    selected = route == r,
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = CarabinerosVerde, selectedTextColor = CarabinerosVerde, indicatorColor = CarabinerosVerdeClaro, unselectedIconColor = TextoPrincipal, unselectedTextColor = TextoPrincipal),
                    onClick = { nav.navigate(r) { popUpTo("inicio") { saveState = true }; launchSingleTop = true; restoreState = true } },
                    icon = { Icon(painterResource(symbols[i]), contentDescription = null) }, label = { Text(title) }
                ) }
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            state.error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp)) }
            NavHost(nav, startDestination = startRoute, modifier = Modifier.weight(1f)) {
                composable("bienvenida") { Welcome { vm.save(mapOf("welcomed" to "true")); nav.navigate("inicio") { popUpTo("bienvenida") { inclusive = true } } } }
                composable("inicio") { Dashboard(state, ::go) }
                composable("unidades") { Units { go("etapa/$it") } }
                composable("etapa/{id}") { back -> Stage(back.arguments?.getString("id")?.toIntOrNull() ?: 1) }
                composable("anteproyecto") { Project(state, vm::save) }
                composable("avances") { Advances(state, vm::save) }
                composable("tutor") { Tutor() }
                composable("biblioteca") { Library() }
                composable("perfil") { Profile(state, vm::save) }
            }
        }
    }
    }
}

@Composable private fun Page(content: @Composable ColumnScope.() -> Unit) {
    val size = rememberWindowSizeClass()
    val contentWidth = when {
        size.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND) -> 900.dp
        size.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) -> 720.dp
        else -> 600.dp
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        Column(Modifier.widthIn(max = contentWidth).fillMaxWidth().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp), content = content)
    }
}
@Composable private fun Panel(content: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth().animateContentSize(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = TarjetaBlanca), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp), content = content)
    }
}
@Composable private fun Heading(title: String, detail: String = "") {
    Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = CarabinerosVerde)
    if (detail.isNotEmpty()) Text(detail, style = MaterialTheme.typography.bodyMedium)
}
@Composable private fun Progress(value: Float) {
    val animated by animateFloatAsState(value, label = "progreso")
    Panel {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("Mi progreso general", fontWeight = FontWeight.Bold); Text("${(value * 100).toInt()}%", color = CarabinerosVerde, fontWeight = FontWeight.Bold) }
        LinearProgressIndicator(progress = { animated }, modifier = Modifier.fillMaxWidth().height(9.dp).clip(CircleShape), color = CarabinerosVerde, trackColor = CarabinerosVerdeClaro)
        Text("Apartados del anteproyecto completados", style = MaterialTheme.typography.labelMedium)
    }
}
@Composable private fun Welcome(start: () -> Unit) {
    Column(Modifier.fillMaxSize().background(CarabinerosVerde).padding(32.dp).verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(26.dp, Alignment.CenterVertically)) {
        EsucarLogo(Modifier.size(width = 152.dp, height = 196.dp))
        Text("Tutor Virtual\nESUCAR", color = TarjetaBlanca, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
        Text("Trabajo de Título I, II y III", color = TarjetaBlanca, style = MaterialTheme.typography.titleLarge)
        Text("Tu guía para avanzar, organizar tus ideas y construir tu anteproyecto.", color = TarjetaBlanca, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = start, colors = ButtonDefaults.buttonColors(containerColor = EsucarDorado, contentColor = TextoPrincipal), modifier = Modifier.fillMaxWidth().height(54.dp)) { Text("Comenzar  →") }
        Text("Contenido disponible sin conexión", color = CarabinerosVerdeClaro)
    }
}
@Composable private fun Dashboard(state: TutorState, go: (String) -> Unit) = Page {
    Box(Modifier.width(40.dp).height(4.dp).background(EsucarDorado, CircleShape))
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        EsucarLogo(Modifier.size(width = 54.dp, height = 70.dp))
        Column(Modifier.weight(1f)) {
            Heading("Bienvenido al Tutor Virtual", state.value("name").ifBlank { "Continúa con el desarrollo de tu anteproyecto." })
        }
    }
    Progress(state.progress)
    Text("Tu espacio de aprendizaje", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
    val links = listOf(Triple("unidades", R.drawable.ic_book, "Mis Unidades"), Triple("anteproyecto", R.drawable.ic_edit, "Mi Anteproyecto"), Triple("tutor", R.drawable.ic_chat, "Tutor Virtual"), Triple("avances", R.drawable.ic_chart, "Mis avances"), Triple("biblioteca", R.drawable.ic_library, "Biblioteca"), Triple("perfil", R.drawable.ic_person, "Mi perfil"))
    Box {
        val columns = if (rememberWindowSizeClass().isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) 3 else 2
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            links.chunked(columns).forEach { group -> Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                group.forEach { (route, icon, label) -> Card(onClick = { go(route) }, modifier = Modifier.weight(1f).fillMaxHeight().heightIn(min = 112.dp), colors = CardDefaults.cardColors(containerColor = TarjetaBlanca), shape = RoundedCornerShape(18.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) { Icon(painterResource(icon), contentDescription = null, tint = CarabinerosVerde, modifier = Modifier.size(30.dp)); Text(label, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center) }
                } }
            } }
        }
    }
    Panel { Text("Un paso a la vez", fontWeight = FontWeight.Bold, color = CarabinerosVerde); Text("Delimita el problema antes de redactar tus objetivos. Confirma los criterios de cada entrega con tu profesor guía.") }
}
@Composable private fun Units(open: (Int) -> Unit) = Page {
    Heading("Guía Metodológica ESUCAR", "Explora las etapas y aplica sus orientaciones a tu trabajo.")
    GuiaRepository.etapas.forEach { stage -> Panel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = CircleShape, color = CarabinerosVerdeClaro) { Text("${stage.id}", Modifier.padding(14.dp), color = CarabinerosVerde, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.width(12.dp)); Text(stage.titulo, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        Text(stage.descripcion)
        TextButton(onClick = { open(stage.id) }) { Text("Leer orientaciones →") }
    } }
}
@Composable private fun Stage(id: Int) = Page {
    val stage = GuiaRepository.etapas.firstOrNull { it.id == id }
    if (stage != null) {
        Heading(stage.titulo, stage.descripcion)
        stage.orientaciones.forEachIndexed { i, text -> Panel { Text("${i + 1}. $text", style = MaterialTheme.typography.bodyLarge) } }
        Text("Orientación didáctica. La pauta y las instrucciones de tu docente determinan los requisitos de entrega.", style = MaterialTheme.typography.bodySmall)
    }
}
@Composable private fun Project(state: TutorState, save: (Map<String, String>) -> Unit) = Page {
    Heading("Construye tu anteproyecto", "Completa los apartados y guarda cada borrador antes de salir.")
    Progress(state.progress)
    AcademicContent.sections.forEachIndexed { i, title ->
        var expanded by rememberSaveable { mutableStateOf(false) }
        var draft by rememberSaveable(state.value("draft_$i")) { mutableStateOf(state.value("draft_$i")) }
        var error by rememberSaveable { mutableStateOf(false) }
        Panel {
            Row(Modifier.fillMaxWidth().clickable { expanded = !expanded }, verticalAlignment = Alignment.CenterVertically) {
                Text("${i + 1}", color = CarabinerosVerde, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 14.dp))
                Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(if (state.value("done_$i") == "true") "✓" else if (expanded) "−" else "+", color = CarabinerosVerde)
            }
            if (expanded) {
                Text(AcademicContent.prompts[i])
                OutlinedTextField(value = draft, onValueChange = { draft = it; error = false }, label = { Text("Tu borrador") }, modifier = Modifier.fillMaxWidth(), minLines = 4, isError = error, supportingText = { Text(if (error) "Escribe al menos 20 caracteres para completar el apartado." else "${draft.trim().length} caracteres · puedes guardar un borrador incompleto") })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = { save(mapOf("draft_$i" to draft, "done_$i" to "false")) }) { Text("Guardar") }
                    Button(onClick = { if (draft.trim().length < 20) error = true else save(mapOf("draft_$i" to draft, "done_$i" to "true")) }) { Text("Completar") }
                }
            }
        }
    }
    Panel { Text("Resumen guardado", fontWeight = FontWeight.Bold); AcademicContent.sections.forEachIndexed { i, title -> if (state.value("draft_$i").isNotBlank()) { Text(title, color = CarabinerosVerde, fontWeight = FontWeight.Bold); Text(state.value("draft_$i")) } } }
}
@Composable private fun Tutor() = Page {
    var query by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("Todas") }
    Heading("Resuelve tus dudas", "Tutor de consultas guiadas · contenido local de apoyo")
    OutlinedTextField(query, { query = it }, label = { Text("Escribe tu consulta o palabra clave") }, modifier = Modifier.fillMaxWidth())
    val categories = listOf("Todas", "Metodología", "Citación", "Objetivos", "Formato", "Entregas")
    categories.chunked(3).forEach { row -> Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { row.forEach { FilterChip(selected = category == it, onClick = { category = it }, label = { Text(it) }) } } }
    val results = AcademicContent.faqs.filter { (category == "Todas" || it.category == category) && (query.isBlank() || (it.question + it.answer).contains(query.trim(), ignoreCase = true)) }
    if (results.isEmpty()) Panel { Text("No encontramos una respuesta. Prueba con «objetivo», «problema» o «referencia», o consulta a tu profesor guía.") }
    results.forEach { faq -> Panel { Text(faq.category, color = CarabinerosVerde, style = MaterialTheme.typography.labelMedium); Text(faq.question, fontWeight = FontWeight.Bold); Text(faq.answer) } }
}
@Composable private fun Advances(state: TutorState, save: (Map<String, String>) -> Unit) = Page {
    Heading("Mis Hitos Académicos", "Registra tus envíos. La aprobación corresponde al profesor guía.")
    Progress(state.progress)
    AcademicContent.hitos(state.values).forEachIndexed { i, hito ->
        var date by rememberSaveable(state.value("date_$i")) { mutableStateOf(state.value("date_$i")) }
        var invalid by rememberSaveable { mutableStateOf(false) }
        Panel {
            Text("Hito ${hito.id}: ${hito.titulo}", fontWeight = FontWeight.Bold)
            Text(when(hito.estado) { EstadoHito.ENVIADO -> "Enviado · pendiente de revisión docente"; EstadoHito.EN_DESARROLLO -> "En desarrollo"; EstadoHito.APROBADO -> "Aprobado por el docente"; else -> "Por iniciar" }, color = CarabinerosVerde)
            OutlinedTextField(date, { date = it; invalid = false }, label = { Text("Fecha límite (AAAA-MM-DD)") }, singleLine = true, modifier = Modifier.fillMaxWidth(), isError = invalid)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = {
                    val parsed = runCatching { java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ROOT).apply { isLenient = false }.parse(date) }.getOrNull()
                    invalid = !date.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) || parsed == null
                    if (!invalid) save(mapOf("date_$i" to date))
                }) { Text("Guardar fecha") }
                TextButton(onClick = { save(mapOf("sent_$i" to if (state.value("sent_$i") == "true") "false" else "true")) }) { Text(if (state.value("sent_$i") == "true") "Deshacer envío" else "Marcar enviado") }
            }
            if (invalid) Text("Ingresa una fecha válida, por ejemplo 2026-12-15.", color = MaterialTheme.colorScheme.error)
        }
    }
    ReminderSettings(state)
}
@Composable private fun Library() = Page {
    val context = LocalContext.current
    Heading("Biblioteca", "Orientaciones de apoyo disponibles sin conexión")
    listOf("Estructura del anteproyecto" to "Problema, delimitación, objetivos, preguntas, justificación y marco conceptual. Ajusta esta estructura a la pauta de tu docente.", "Revisión antes de entregar" to "Verifica coherencia entre problema y objetivos, fuentes citadas, ortografía, formato solicitado, nombre de archivo y fecha límite.", "Criterios de autoevaluación" to "¿El problema está delimitado? ¿Los objetivos son alcanzables? ¿El método responde a las preguntas? ¿Cada afirmación relevante tiene sustento?").forEach { (title, text) -> Panel { Text(title, fontWeight = FontWeight.Bold, color = CarabinerosVerde); Text(text) } }
    Panel {
        Text("Recursos externos · requieren conexión", fontWeight = FontWeight.Bold)
        TextButton(onClick = { runCatching { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://apastyle.apa.org/style-grammar-guidelines/references"))) } }) { Text("Consultar referencias en APA Style ↗") }
        Text("Las pautas y rúbricas institucionales deberán incorporarse cuando se disponga de material autorizado.", style = MaterialTheme.typography.bodySmall)
    }
}
@Composable private fun Profile(state: TutorState, save: (Map<String, String>) -> Unit) = Page {
    val context = LocalContext.current
    var name by rememberSaveable { mutableStateOf(state.value("name")) }
    var email by rememberSaveable { mutableStateOf(state.value("email")) }
    var course by rememberSaveable { mutableStateOf(state.value("course").ifBlank { "Trabajo de Título I" }) }
    var error by rememberSaveable { mutableStateOf(false) }
    var cameraUri by rememberSaveable { mutableStateOf("") }
    var photoError by remember { mutableStateOf("") }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let { runCatching {
            context.contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            save(mapOf("photo" to it.toString()))
        }.onFailure { photoError = "No se pudo abrir la imagen seleccionada." } }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success -> if (success) save(mapOf("photo" to cameraUri)) }
    Heading("Mi perfil", "Personaliza tu espacio académico")
    val bitmap = remember(state.value("photo")) { runCatching {
        context.contentResolver.openInputStream(Uri.parse(state.value("photo")))?.use { BitmapFactory.decodeStream(it, null, BitmapFactory.Options().apply { inSampleSize = 4 }) }
    }.getOrNull() }
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (bitmap != null) Image(bitmap.asImageBitmap(), "Foto de perfil", Modifier.size(112.dp).clip(CircleShape), contentScale = ContentScale.Crop)
        else Surface(shape = CircleShape, color = CarabinerosVerdeClaro) { Text("ESUCAR", Modifier.padding(28.dp), color = CarabinerosVerde, fontWeight = FontWeight.Bold) }
    }
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = { gallery.launch(arrayOf("image/*")) }) { Text("Galería") }
        OutlinedButton(onClick = { runCatching {
            val dir = File(context.filesDir, "photos").apply { mkdirs() }
            val file = File.createTempFile("perfil_", ".jpg", dir)
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.files", file)
            cameraUri = uri.toString(); camera.launch(uri)
        }.onFailure { photoError = "No se pudo abrir la cámara en este dispositivo." } }) { Text("Cámara") }
    }
    if (photoError.isNotBlank()) Text(photoError, color = MaterialTheme.colorScheme.error)
    Panel {
        OutlinedTextField(name, { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth(), isError = error && name.isBlank())
        OutlinedTextField(email, { email = it }, label = { Text("Correo") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(), isError = error && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        OutlinedTextField(course, { course = it }, label = { Text("Asignatura") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = { error = name.isBlank() || course.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches(); if (!error) save(mapOf("name" to name.trim(), "email" to email.trim(), "course" to course.trim())) }) { Text("Guardar perfil") }
        if (error) Text("Completa tu nombre, asignatura y un correo válido.", color = MaterialTheme.colorScheme.error)
    }
    if (state.value("name").isNotBlank()) Panel { Text("Perfil guardado", fontWeight = FontWeight.Bold); Text(state.value("name")); Text(state.value("email")); Text(state.value("course")) }
    Progress(state.progress)
}
