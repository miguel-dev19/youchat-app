package cu.youchat.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cu.youchat.app.MainViewModel
import cu.youchat.app.ui.screens.*

@Composable
fun YouChatNavHost(navController: NavHostController) {
    val viewModel: MainViewModel = hiltViewModel()
    val startDest by viewModel.startDestination.collectAsState()

    NavHost(navController = navController, startDestination = startDest) {
        composable(Rutas.ONBOARDING) {
            OnboardingScreen(onFinalizar = {
                viewModel.setOnboardingComplete()
                navController.navigate(Rutas.LOGIN) { popUpTo(Rutas.ONBOARDING) { inclusive = true } }
            })
        }
        composable(Rutas.LOGIN) {
            LoginScreen(onLoginExitoso = {
                viewModel.setLoginComplete()
                navController.navigate(Rutas.WELCOME_PERFIL) { popUpTo(Rutas.LOGIN) { inclusive = true } }
            })
        }
        composable(Rutas.WELCOME_PERFIL) {
            WelcomePerfilScreen(onContinuar = {
                viewModel.setWelcomeComplete()
                navController.navigate(Rutas.PRINCIPAL) { popUpTo(Rutas.WELCOME_PERFIL) { inclusive = true } }
            })
        }
        composable(Rutas.PRINCIPAL) {
            PrincipalScreen(
                onNavigateToChat = { _, _ -> },
                onNavigateToPerfil = { navController.navigate(Rutas.VIEW_YOU_PERFIL) },
                onNavigateToContactos = { navController.navigate(Rutas.CONTACTOS) }
            )
        }
        composable(Rutas.VIEW_YOU_PERFIL) {
            ViewYouPerfilScreen(onBack = { navController.popBackStack() }, onNavigateToEditPerfil = { campo -> navController.navigate(Rutas.editPerfil(campo)) })
        }
        composable(Rutas.CONTACTOS) {
            ContactosScreen(
                onBack = { navController.popBackStack() },
                onContactoSeleccionado = { _, _ -> }
            )
        }
        composable(Rutas.EDIT_PERFIL, arguments = listOf(navArgument("campo") { type = NavType.StringType })) { backStackEntry ->
            val campo = backStackEntry.arguments?.getString("campo") ?: "alias"
            EditPerfilScreen(campo = campo, onBack = { navController.popBackStack() })
        }
    }
}
