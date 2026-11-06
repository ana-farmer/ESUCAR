package cl.duoc.tutorvirtual.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.computeWindowSizeClass

@Composable fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenWidthDp, configuration.screenHeightDp) {
        WindowSizeClass.BREAKPOINTS_V1.computeWindowSizeClass(configuration.screenWidthDp, configuration.screenHeightDp)
    }
}
