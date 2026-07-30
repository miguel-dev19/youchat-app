package cu.youchat.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cu.youchat.app.YouChatApplication
import cu.youchat.app.ui.screens.*

@Composable
fun YouChatNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = when (YouChatApplication.mark) {
            0 -> Rutas.ONBOARDING
            1 -> Rutas.LOGIN
            2 -> Rutas.WELCOME_PERFIL
            else -> Rutas.PRINCIPAL
        }
    ) {
        composable(Rutas.ONBOARDING) {
            OnboardingScreen(onFinalizar = {
                YouChatApplication.mark = 1
                navController.navigate(Rutas.LOGIN) { popUpTo(Rutas.ONBOARDING) { inclusive = true } }
            })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(onLoginExitoso = {
                navController.navigate(Rutas.WELCOME_PERFIL) { popUpTo(Rutas.LOGIN) { inclusive = true } }
            })
        }
        composable(Rutas.WELCOME_PERFIL) {
            WelcomePerfilScreen(onContinuar = {
                YouChatApplication.mark = 3
                navController.navigate(Rutas.PRINCIPAL) { popUpTo(Rutas.WELCOME_PERFIL) { inclusive = true } }
            })
        }
        composable(Rutas.PRINCIPAL) {
            PrincipalScreen(
                onNavigateToChat = { correo, nombre -> },
                onNavigateToPerfil = { },
                onNavigateToContactos = { }
            )
        }
    }
}
