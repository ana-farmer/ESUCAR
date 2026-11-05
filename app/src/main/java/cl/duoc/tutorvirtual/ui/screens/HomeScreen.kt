package cl.duoc.tutorvirtual.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.tutorvirtual.ui.theme.CarabinerosVerde
import cl.duoc.tutorvirtual.ui.theme.CarabinerosVerdeClaro
import cl.duoc.tutorvirtual.ui.theme.EsucarDorado


data class AreaTemario(
    val titulo: String,
    val subtitulo: String,
    val icono: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    onNavigateToGuia: () -> Unit = {},
    onNavigateToFaq: () -> Unit = {},
    onNavigateToHitos: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {}
)}
val scrollState = rememberScrollState()
//material Temario
val areasTemario = listOf(
    AreaTemario(
        "Doctrina Institucional",
        "Principios y Ética Policial",
        Icons.Default.Security
    ),
    AreaTemario(
        "Área Jurídica",
        "Código Procesal Penal y Leyes",
        Icons.Default.Gavel
    ),
    AreaTemario(
        "Ciencias Policiales",
        "Criminología y Plan Cuadrante",
        Icons.Default.Book
    ),
    AreaTemario(
        "Desarrollo Personal",
        "Emociones y Comunicación",
        Icons.Default.Psychology
    )
)
}











