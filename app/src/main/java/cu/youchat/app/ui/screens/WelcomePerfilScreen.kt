package cu.youchat.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cu.youchat.app.YouChatApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomePerfilScreen(onContinuar: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var alias by remember { mutableStateOf("Usuario" ?: "") }
    var rutaImagenPerfil by remember { mutableStateOf("" ?: "") }
    var configSeleccionada by remember { mutableIntStateOf(2) }
    var verificandoBD by remember { mutableStateOf(false) }
    var hayCopiaSeguridad by remember { mutableStateOf<Boolean?>(null) }
    var rutaCopia by remember { mutableStateOf("") }
    var cargandoBD by remember { mutableStateOf(false) }
    var textoBandeja by remember { mutableStateOf("Obteniendo cantidad de correos y peso total...") }
    var mostrarProgreso by remember { mutableStateOf(true) }
    var mostrarReintentar by remember { mutableStateOf(false) }
    var mostrarVaciar by remember { mutableStateOf(false) }
    val colorBtn = Color(0xFF3F51B5)

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            try {
                val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                val nombreImg = ("" ?: "user").replace(".", "").replace("@", "") + sdf.format(Date()) + ".jpg"
                val miPath = "/YouChat/.Imagenes de perfil/" + nombreImg
                File("/YouChat/.Imagenes de perfil/").mkdirs()
                context.contentResolver.openInputStream(it)?.use { input -> FileOutputStream(File(miPath)).use { output -> input.copyTo(output) } }
                rutaImagenPerfil = miPath
                "" = miPath
            } catch (_: Exception) {}
        }
    }

    val descripcion = when (configSeleccionada) {
        1 -> "Serán desactivadas todas las funciones que consuman datos de la aplicación.\nTodos estos ajustes pueden ser modificados luego."
        2 -> "Serán activadas sólo las funciones más importantes para el uso de la aplicación.\nTodos estos ajustes pueden ser modificados luego."
        3 -> "Serán activadas todas las funciones que consuman datos (con moderación en la configuración), para una mejor experiencia y uso.\nTodos estos ajustes pueden ser modificados luego."
        else -> ""
    }

    fun verificarCopiaSeguridad() {
        scope.launch {
            verificandoBD = true; hayCopiaSeguridad = null
            withContext(Dispatchers.IO) {
                val sd = File("/YouChat/")
                if (!sd.exists()) sd.mkdirs()
                val backupDB = File(sd, "YouChat_BDatos.dbyc")
                hayCopiaSeguridad = backupDB.exists()
                if (backupDB.exists()) rutaCopia = backupDB.absolutePath
            }
            verificandoBD = false
        }
    }

    fun verificarBandeja() {
        scope.launch {
            mostrarProgreso = true; mostrarReintentar = false; mostrarVaciar = false
            textoBandeja = "Obteniendo cantidad de correos y peso total..."
            try {
                val resultado = withContext(Dispatchers.IO) { analizarBandeja() }
                if (resultado != null) {
                    val (cant, peso) = resultado
                    mostrarProgreso = false; mostrarVaciar = true
                    textoBandeja = if (cant == 1) "Encontrado 1 correo, con peso total igual a ${formatearBytes(peso)}"
                    else "Encontrados $cant correos, con peso total igual a ${formatearBytes(peso)}"
                } else {
                    mostrarProgreso = false; mostrarReintentar = true
                    textoBandeja = "Falló al intentar escanear la bandeja, vuelva a intentar."
                }
            } catch (_: Exception) {
                mostrarProgreso = false; mostrarReintentar = true
                textoBandeja = "Falló al intentar escanear la bandeja, vuelva a intentar."
            }
        }
    }

    fun vaciarBandeja() {
        scope.launch {
            mostrarVaciar = false; mostrarProgreso = true
            textoBandeja = "Vaciando bandeja, por favor espere..."
            try { withContext(Dispatchers.IO) { vaciarBandejaIMAP() }; textoBandeja = "Bandeja vaciada correctamente" }
            catch (_: Exception) { textoBandeja = "Error al vaciar la bandeja" }
            mostrarProgreso = false; mostrarReintentar = true; mostrarVaciar = false
        }
    }

    LaunchedEffect(Unit) { verificarBandeja() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Editar perfil", fontWeight = FontWeight.Bold, fontSize = 20.sp) }, colors = TopAppBarDefaults.topAppBarColors(containerColor = colorBtn, titleContentColor = Color.White)) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                preferences.setAlias(alias)
                // Configuracion: $configSeleccionada
                // mark se actualiza en NavHost onContinuar()
            }, containerColor = colorBtn) { Icon(Icons.Filled.Check, "Continuar", tint = Color.White) }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().background(Color.White).padding(padding).verticalScroll(rememberScrollState()).padding(bottom = 75.dp)) {
            // Foto + alias
            Card(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 5.dp), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), shape = RoundedCornerShape(8.dp)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        if (rutaImagenPerfil.isNotEmpty()) {
                            AsyncImage(model = ImageRequest.Builder(context).data(rutaImagenPerfil).crossfade(true).build(), contentDescription = "Foto", modifier = Modifier.size(140.dp).clip(CircleShape).border(2.dp, colorBtn, CircleShape), contentScale = ContentScale.Crop)
                        } else {
                            Icon(Icons.Filled.AccountCircle, "Foto", modifier = Modifier.size(140.dp).clip(CircleShape).border(2.dp, colorBtn, CircleShape), tint = Color.Gray)
                        }
                        SmallFloatingActionButton(onClick = { galleryLauncher.launch("image/*") }, containerColor = colorBtn, modifier = Modifier.size(45.dp)) { Icon(Icons.Filled.CameraAlt, "Cambiar", tint = Color.White, modifier = Modifier.size(22.dp)) }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(value = alias, onValueChange = { alias = it }, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp), label = { Text("Alias") }, leadingIcon = { Icon(Icons.Filled.Badge, null) }, singleLine = true, shape = RoundedCornerShape(12.dp))
                }
            }
            // Configuración
            Card(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), shape = RoundedCornerShape(8.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Configuración inicial:", fontSize = 17.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(8.dp))
                    RadioButtonItem("Modo Ahorro", configSeleccionada == 1) { configSeleccionada = 1 }
                    RadioButtonItem("Modo Estándar", configSeleccionada == 2) { configSeleccionada = 2 }
                    RadioButtonItem("Modo Completo", configSeleccionada == 3) { configSeleccionada = 3 }
                }
            }
            Text(descripcion, fontSize = 13.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp))
            // Copia seguridad
            Card(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), shape = RoundedCornerShape(8.dp)) {
                Column(Modifier.padding(12.dp)) {
                    Text("Copia de seguridad:", fontSize = 17.sp, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.height(5.dp))
                    Row(Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.Top) { Icon(Icons.Filled.Info, null, Modifier.size(20.dp), tint = Color.Gray); Spacer(Modifier.width(8.dp)); Text(if (hayCopiaSeguridad == true) "Copia en: $rutaCopia" else if (hayCopiaSeguridad == false) "No existen copias" else "Toque Verificar para buscar", fontSize = 15.sp, color = Color.Gray) }
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { verificarCopiaSeguridad() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), enabled = !verificandoBD, colors = ButtonDefaults.buttonColors(containerColor = colorBtn), shape = RoundedCornerShape(8.dp)) { if (verificandoBD) { CircularProgressIndicator(Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White); Spacer(Modifier.width(8.dp)) }; Text("Verificar", color = Color.White) }
                    AnimatedVisibility(visible = hayCopiaSeguridad == true) { Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp), enabled = !cargandoBD, colors = ButtonDefaults.buttonColors(containerColor = colorBtn), shape = RoundedCornerShape(8.dp)) { Text("Cargar", color = Color.White) } }
                }
            }
            // Bandeja
            Card(Modifier.fillMaxWidth().padding(horizontal = 6.dp, vertical = 4.dp), elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), shape = RoundedCornerShape(8.dp)) {
                Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Bandeja de entrada:", fontSize = 17.sp, fontWeight = FontWeight.Medium, modifier = Modifier.align(Alignment.Start))
                    Spacer(Modifier.height(12.dp))
                    AnimatedVisibility(visible = mostrarProgreso) { CircularProgressIndicator(Modifier.size(48.dp), strokeWidth = 4.dp) }
                    Spacer(Modifier.height(8.dp))
                    Text(textoBandeja, fontSize = 15.sp, textAlign = TextAlign.Center, color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp))
                    Spacer(Modifier.height(12.dp))
                    AnimatedVisibility(visible = mostrarReintentar) { Button(onClick = { verificarBandeja() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = colorBtn), shape = RoundedCornerShape(8.dp)) { Text("Reintentar", color = Color.White) } }
                    AnimatedVisibility(visible = mostrarVaciar) { Button(onClick = { vaciarBandeja() }, modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), colors = ButtonDefaults.buttonColors(containerColor = colorBtn), shape = RoundedCornerShape(8.dp)) { Text("Vaciar bandeja", color = Color.White) } }
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RadioButtonItem(texto: String, seleccionado: Boolean, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 4.dp, horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) { RadioButton(selected = seleccionado, onClick = onClick); Spacer(Modifier.width(8.dp)); Text(texto, fontSize = 15.sp) }
}

private suspend fun analizarBandeja(): Pair<Int, Long>? = withContext(Dispatchers.IO) {
    try {
        val props = Properties().apply { setProperty("mail.store.protocol", "imap"); setProperty("mail.imap.host", "imap.nauta.cu"); setProperty("mail.imap.port", "143") }
        val session = Session.getDefaultInstance(props, object : Authenticator() { override fun getPasswordAuthentication() = PasswordAuthentication("", YouChatApplication.pass) })
        val store = session.getStore("imap") as IMAPStore; store.connect("imap.nauta.cu", "", YouChatApplication.pass)
        if (store.isConnected) { val inbox = store.getFolder("Inbox") as IMAPFolder; inbox.open(Folder.READ_WRITE); if (inbox.isOpen) { val messages = inbox.getMessages(); val cant = messages.size; var peso = 0L; for (msg in messages) peso += msg.size; inbox.close(false); store.close(); return@withContext Pair(cant, peso) }; store.close() }
    } catch (_: Exception) {}
    null
}

private suspend fun vaciarBandejaIMAP() = withContext(Dispatchers.IO) {
    try {
        val props = Properties().apply { setProperty("mail.store.protocol", "imap"); setProperty("mail.imap.host", "imap.nauta.cu"); setProperty("mail.imap.port", "143") }
        val session = Session.getDefaultInstance(props, object : Authenticator() { override fun getPasswordAuthentication() = PasswordAuthentication("", YouChatApplication.pass) })
        val store = session.getStore("imap") as IMAPStore; store.connect("imap.nauta.cu", "", YouChatApplication.pass)
        if (store.isConnected) { val inbox = store.getFolder("Inbox") as IMAPFolder; inbox.open(Folder.READ_WRITE); if (inbox.isOpen) { for (msg in inbox.getMessages()) msg.setFlag(Flags.Flag.DELETED, true); inbox.close(true); store.close() } }
    } catch (_: Exception) {}
}

private fun formatearBytes(bytes: Long): String = when { bytes < 1024 -> "$bytes B"; bytes < 1024 * 1024 -> "%.1f KB".format(bytes / 1024.0); else -> "%.1f MB".format(bytes / 1024.0 / 1024.0) }
