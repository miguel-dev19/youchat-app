package cu.youchat.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import cu.youchat.app.R
import cu.youchat.app.YouChatApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.mail.*
import com.sun.mail.smtp.SMTPTransport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onLoginExitoso: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val colorTema = Color(0xFF3F51B5)
    val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.aasend_e_mail_egpid))
    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComposition, isPlaying = isLoading, iterations = LottieConstants.IterateForever
    )

    fun mostrarError(mensaje: String) {
        errorMessage = mensaje
        scope.launch { delay(3000); errorMessage = null }
    }

    fun intentarLogin() {
        val correo = email.trim()
        val pass = password.trim()
        when {
            correo.isEmpty() -> { mostrarError("El correo no puede estar vacío"); return }
            !correo.endsWith("@nauta.cu") -> { mostrarError("Debe ser un correo Nauta (@nauta.cu)"); return }
            pass.isEmpty() -> { mostrarError("La contraseña no puede estar vacía"); return }
        }
        focusManager.clearFocus()
        isLoading = true
        scope.launch {
            try {
                val exito = verificarCorreoNauta(correo, pass)
                if (exito) {
                    YouChatApplication.correo = correo
                    YouChatApplication.pass = pass
                    YouChatApplication.mark = 2
                    delay(600)
                    onLoginExitoso()
                } else throw Exception("Credenciales incorrectas")
            } catch (e: Exception) {
                isLoading = false
                mostrarError("Error: ${e.message}")
            }
        }
    }

    Box(Modifier.fillMaxSize().background(Color(0xFFEDF2F8))) {
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(visible = !isLoading, enter = fadeIn() + scaleIn(), exit = fadeOut() + scaleOut()) {
                Icon(painterResource(R.drawable.iconycvector9), "YouChat", Modifier.size(130.dp), tint = Color.Unspecified)
            }
            Spacer(Modifier.height(24.dp))
            AnimatedVisibility(visible = !isLoading, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    OutlinedTextField(
                        value = email, onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Correo") },
                        leadingIcon = { Icon(Icons.Filled.Person, "Correo", tint = Color.Black) },
                        singleLine = true, shape = RoundedCornerShape(50),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorTema, unfocusedBorderColor = Color(0xFF6B6B6B), cursorColor = Color.Black)
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = password, onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Contraseña") },
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
                        keyboardActions = KeyboardActions(onDone = { intentarLogin() }),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorTema, unfocusedBorderColor = Color(0xFF6B6B6B), cursorColor = Color.Black)
                    )
                    Spacer(Modifier.height(20.dp))
                    Button(
                        onClick = { intentarLogin() },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = colorTema),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) { Text("Autenticar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                }
            }
            AnimatedVisibility(visible = isLoading, enter = fadeIn(animationSpec = tween(300)), exit = fadeOut()) {
                LottieAnimation(composition = lottieComposition, progress = { lottieProgress }, modifier = Modifier.size(220.dp))
            }
        }
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            errorMessage?.let { mensaje ->
                Card(Modifier.fillMaxWidth().padding(16.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer), shape = RoundedCornerShape(12.dp)) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, null, tint = MaterialTheme.colorScheme.onErrorContainer)
                        Spacer(Modifier.width(8.dp))
                        Text(mensaje, Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer)
                        TextButton(onClick = { errorMessage = null }) { Text("OK") }
                    }
                }
            }
        }
    }
}

private suspend fun verificarCorreoNauta(correo: String, pass: String): Boolean = withContext(Dispatchers.IO) {
    try {
        val props = Properties().apply {
            put("mail.smtp.host", "smtp.nauta.cu"); put("mail.smtp.auth", "true"); put("mail.smtp.port", "25")
        }
        val session = Session.getDefaultInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(correo, pass)
        })
        val transport = SMTPTransport(session, javax.mail.URLName("smtp", "smtp.nauta.cu", 25, null, correo, pass))
        if (!transport.isConnected) transport.connect()
        transport.isConnected
    } catch (e: Exception) { false }
}
