package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.tutorvirtual.ui.theme.CarabinerosVerde
import cl.duoc.tutorvirtual.ui.theme.CarabinerosVerdeClaro
import cl.duoc.tutorvirtual.ui.theme.EsucarDorado

data class EtapaGuia(
    val numero: String,
    val titulo: String,
    val descripcion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuiaMetodologicaScreen() {
    val etapas = listOf(
        EtapaGuia(
            "1",
            "Planteamiento del Problema",
            "Definición de objetivos, justificación y preguntas de investigación."
        ),
        EtapaGuia(
            "2",
            "Marco Teórico y Estado del Arte",
            "Recopilación de antecedentes institucionales y marco normativo."
        ),
        EtapaGuia(
            "3",
            "Diseño Metodológico",
            "Definición del enfoque, muestra e instrumentos de recolección."
        ),
        EtapaGuia(
            "4",
            "Análisis de Resultados",
            "Procesamiento de datos y conclusiones operativas para ESUCAR."
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Guía Metodológica ESUCAR",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Trabajo de Título I, II y III",
                            style = MaterialTheme.typography.bodySmall.copy(color = EsucarDorado)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CarabinerosVerde // 🟢 Encabezado Verde Oficial
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "📌 Pasos Oficiales del Proceso",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = CarabinerosVerde,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(etapas) { etapa ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CarabinerosVerdeClaro // 🟢 Fondo Tarjeta Pantone 6197 C
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Círculo Verde con número de etapa
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(CarabinerosVerde, shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = etapa.numero,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = etapa.titulo,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = etapa.descripcion,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}