package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onNavigateToGuia: () -> Unit = {},
    onNavigateToFaq: () -> Unit = {},
    onNavigateToHitos: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Bienvenido al Tutor Virtual", style = MaterialTheme.typography.headlineSmall)
        Text("Apoyo para Trabajo de Título I, II y III.")
        Button(onClick = onNavigateToGuia, modifier = Modifier.fillMaxWidth()) { Text("Guía metodológica") }
        Button(onClick = onNavigateToFaq, modifier = Modifier.fillMaxWidth()) { Text("Preguntas frecuentes") }
        Button(onClick = onNavigateToHitos, modifier = Modifier.fillMaxWidth()) { Text("Mis avances") }
        Button(onClick = onNavigateToPerfil, modifier = Modifier.fillMaxWidth()) { Text("Mi perfil") }
    }
}
