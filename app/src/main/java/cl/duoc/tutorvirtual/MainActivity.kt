package cl.duoc.tutorvirtual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import cl.duoc.tutorvirtual.ui.screens.EsucarApp
import cl.duoc.tutorvirtual.ui.theme.TutorVirtualESUCARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TutorVirtualESUCARTheme(darkTheme = false) { TutorVirtualApp() } }
    }
}
@Composable fun TutorVirtualApp() { EsucarApp() }
