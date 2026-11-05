package cl.duoc.tutorvirtual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.duoc.tutorvirtual.ui.screens.GuiaMetodologicaScreen
import cl.duoc.tutorvirtual.ui.screens.HomeScreen
import cl.duoc.tutorvirtual.ui.screens.HitosScreen
import cl.duoc.tutorvirtual.ui.theme.TutorVirtualESUCARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TutorVirtualESUCARTheme(darkTheme = false, dynamicColor = false) {
                TutorVirtualApp()
            }
        }
    }
}

@Composable
fun TutorVirtualApp() {
    var pantalla by rememberSaveable { mutableStateOf("Inicio") }
    BackHandler(enabled = pantalla != "Inicio") { pantalla = "Inicio" }

    Scaffold(
        // La guía tiene su propio Scaffold y su encabezado gestiona el espacio superior.
        contentWindowInsets = if (pantalla == "Guía metodológica") {
            WindowInsets(0, 0, 0, 0)
        } else {
            ScaffoldDefaults.contentWindowInsets
        },
        topBar = {
            if (pantalla != "Guía metodológica") {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    Text(
                        "Tutor Virtual ESUCAR",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth().padding(20.dp)
                    )
                }
            }
        },
        bottomBar = {
            if (pantalla != "Inicio") {
                Surface {
                    OutlinedButton(
                        onClick = { pantalla = "Inicio" },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
                    ) { Text("Volver al inicio") }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
            when (pantalla) {
                "Inicio" -> HomeScreen(
                    onNavigateToGuia = { pantalla = "Guía metodológica" },
                    onNavigateToFaq = { pantalla = "Preguntas frecuentes" },
                    onNavigateToHitos = { pantalla = "Mis avances" },
                    onNavigateToPerfil = { pantalla = "Mi perfil" }
                )
                "Guía metodológica" -> GuiaMetodologicaScreen()
                "Mis avances" -> HitosScreen()
                else -> Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(pantalla, style = MaterialTheme.typography.headlineMedium)
                    Text("Módulo preparado para próximas actividades.")
                }
            }
        }
    }
}
