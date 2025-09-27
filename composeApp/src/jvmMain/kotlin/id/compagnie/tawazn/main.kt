package id.compagnie.tawazn

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Tawazn",
    ) {
        App()
    }
}