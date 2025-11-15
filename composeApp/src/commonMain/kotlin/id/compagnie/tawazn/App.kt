package id.compagnie.tawazn

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import id.compagnie.tawazn.design.theme.TawaznTheme
import id.compagnie.tawazn.feature.dashboard.DashboardScreen
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        TawaznTheme {
            Navigator(DashboardScreen()) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}
