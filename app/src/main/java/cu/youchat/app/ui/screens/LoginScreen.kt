package cu.youchat.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import cu.youchat.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginExitoso: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }
    val colorTema = Color(0xFF3F51B5)
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.aasend_e_mail_egpid))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComposition, isPlaying = uiState.isLoading, iterations = LottieConstants.IterateForever
    )

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) onLoginExitoso()
    }

    Box(Modifier.fillMaxSize().background(Color(0xFFEDF2F8))) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            AnimatedVisibility(visible = !uiState.isLoading, enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()) {
                Icon(painterResource(R.drawable.iconycvector9), "YouChat", Modifier.size(130.dp), tint = Color.Unspecified)
            }
            Spacer(Modifier.height(24.dp))
            AnimatedVisibility(visible = !uiState.isLoading, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = uiState.email, onValueChange = { viewModel.onEmailChange(it) },
                        modifier = Modifier.fillMaxWidth(), placeholder = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Filled.Person, "Correo", tint = Color.Black) },
                        singleLine = true, shape = RoundedCornerShape(50),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorTema, unfocusedBorderColor = Color(0xFF6B6B6B), cursorColor = Color.Black)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = uiState.password, onValueChange = { viewModel.onPasswordChange(it) },
                        modifier = Modifier.fillMaxWidth(), placeholder = { Text("Contraseña") },
                        leadingIcon = { Icon(Icons.Filled.Lock, "Contraseña", tint = Color.Black) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, "Ver", tint = Color.Black)
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        shape = RoundedCornerShape(50),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { viewModel.login() }),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorTema, unfocusedBorderColor = Color(0xFF6B6B6B), cursorColor = Color.Black)
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(onClick = { viewModel.login() }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(50), colors = ButtonDefaults.buttonColors(containerColor = colorTema), elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)) {
                        Text("Autenticar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
            AnimatedVisibility(visible = uiState.isLoading, enter = fadeIn(animationSpec = tween(300)), exit = fadeOut()) {
                LottieAnimation(composition = lottieComposition, progress = { lottieProgress }, modifier = Modifier.size(220.dp))
            }
        }
        AnimatedVisibility(visible = uiState.error != null, enter = slideInVertically(initialOffsetY = { it }) + fadeIn(), exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(), modifier = Modifier.align(Alignment.BottomCenter)) {
            uiState.error?.let { mensaje ->
                Card(Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(Modifier.width(8.dp))
                        Text(mensaje, Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer)
                        TextButton(onClick = { viewModel.clearError() }) { Text("OK") }
                    }
                }
            }
        }
    }
}
