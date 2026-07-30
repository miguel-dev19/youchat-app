package cu.alexgi.youchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cu.alexgi.youchat.ui.screens.OnboardingScreen

@Composable
fun YouChatNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Rutas.ONBOARDING) {
        composable(Rutas.ONBOARDING) {
            OnboardingScreen(onFinalizar = { navController.navigate(Rutas.LOGIN) })
        }
        composable(Rutas.LOGIN) {
            // Placeholder
        }
    }
}
