package cl.duoc.tutorvirtual.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = CarabinerosVerde,
    onPrimary = TarjetaBlanca,
    primaryContainer = CarabinerosVerdeClaro,
    onPrimaryContainer = TextoOscuro,
    secondary = EsucarDorado,
    onSecondary = TarjetaBlanca,
    background = FondoSuperficie,
    surface = TarjetaBlanca,
    onSurface = TextoOscuro
)

private val DarkColorScheme = darkColorScheme(
    primary = CarabinerosVerde,
    onPrimary = TarjetaBlanca,
    primaryContainer = CarabinerosVerdeClaro,
    onPrimaryContainer = TextoOscuro,
    secondary = EsucarDorado,
    onSecondary = TarjetaBlanca
)

@Composable
fun TutorVirtualESUCARTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // IMPORTANTE: dynamicColor en false para forzar el verde institucional en Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}