package cu.alexgi.youchat.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cu.alexgi.youchat.YouChatApplication
import cu.alexgi.youchat.ui.screens.*

@Composable
fun YouChatNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = when (YouChatApplication.mark) {
            0 -> Rutas.ONBOARDING
            else -> Rutas.LOGIN
        }
    ) {
        composable(Rutas.ONBOARDING) {
            OnboardingScreen(onFinalizar = {
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(Rutas.ONBOARDING) { inclusive = true }
                }
            })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(onLoginExitoso = {
                // Aquí irá WelcomePerfilScreen después
            })
        }
    }
}
