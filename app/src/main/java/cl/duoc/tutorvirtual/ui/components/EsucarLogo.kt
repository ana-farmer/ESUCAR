package cl.duoc.tutorvirtual.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import cl.duoc.tutorvirtual.R
import kotlin.math.roundToInt

/** Crop the exterior of the supplied 480px emblem without redrawing its artwork. */
@Composable
fun EsucarLogo(modifier: Modifier = Modifier) {
    val original = ImageBitmap.imageResource(R.drawable.esucar_logo)
    Canvas(modifier.semantics { contentDescription = "Escudo de la Escuela de Suboficiales de Carabineros" }) {
        val shield = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height * 357f / 454f)
            lineTo(size.width * 177f / 353f, size.height)
            lineTo(0f, size.height * 357f / 454f)
            close()
        }
        clipPath(shield) {
            drawImage(
                image = original,
                srcOffset = IntOffset(58, 13),
                srcSize = IntSize(353, 454),
                dstSize = IntSize(size.width.roundToInt(), size.height.roundToInt())
            )
        }
    }
}
