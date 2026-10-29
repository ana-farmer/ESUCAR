package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.tutorvirtual.ui.theme.*

@Composable
fun HitosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Mis Hitos Académicos",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = CarabinerosVerde
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                HitoCardItem(
                    "TT-101",
                    "Informe de Avance I",
                    "15 Oct 2025",
                    "Aprobado (6.8)"
                )
            }
            item {
                HitoCardItem(
                    "TT-102",
                    "Revisión Metodológica APA",
                    "30 Oct 2025",
                    "Enviado"
                )
            }
            item {
                HitoCardItem(
                    "TT-103",
                    "Proyecto Final de Titulación",
                    "15 Nov 2025",
                    "Por Iniciar"
                )
            }
        }
    }
}


@Composable
fun HitoCardItem(
    codigo: String,
    titulo: String,
    fecha: String,
    estado: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "[$codigo] $titulo",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fecha límite: $fecha",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Surface(
                color = if (estado.contains("Aprobado")) EstadoAprobado else CarabinerosVerde,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = estado,
                    color = Color.White, `
                            modifier = Modifier.padding
                )

            }
        }
    }
}
}





































