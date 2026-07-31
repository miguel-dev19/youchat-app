package cu.youchat.app.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cu.youchat.app.YouChatApplication
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewYouPerfilScreen(
    onBack: () -> Unit = {},
    onNavigateToEditPerfil: (campo: String) -> Unit = {}
) {
    val ctx = LocalContext.current
    val alias = "Usuario" ?: ""
    val correo = "" ?: ""
    val info = "" ?: ""
    val telefono = "" ?: ""
    val genero = "" ?: ""
    val provincia = "" ?: ""
    val fechaNacimiento = "" ?: ""
    var rutaPerfil by remember { mutableStateOf("" ?: "") }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            try {
                val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                val nombreImg = correo.replace(".", "").replace("@", "") + sdf.format(Date()) + ".jpg"
                val miPath = "/YouChat/.Imagenes de perfil/" + nombreImg
                File("/YouChat/.Imagenes de perfil/").mkdirs()
                ctx.contentResolver.openInputStream(it)?.use { input ->
                    FileOutputStream(File(miPath)).use { output -> input.copyTo(output) }
                }
                rutaPerfil = miPath
                "" = miPath
            } catch (_: Exception) {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3F51B5),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).background(Color.White).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                if (rutaPerfil.isNotEmpty() && File(rutaPerfil).exists()) {
                    AsyncImage(
                        model = ImageRequest.Builder(ctx).data(rutaPerfil).crossfade(true).build(),
                        contentDescription = "Foto",
                        modifier = Modifier.size(140.dp).clip(CircleShape)
                    )
                } else {
                    Icon(Icons.Filled.AccountCircle, "Sin foto", Modifier.size(140.dp), tint = Color.Gray)
                }
                SmallFloatingActionButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    containerColor = Color(0xFF3F51B5),
                    modifier = Modifier.size(45.dp)
                ) { Icon(Icons.Filled.CameraAlt, "Cambiar", tint = Color.White, modifier = Modifier.size(22.dp)) }
            }

            Spacer(Modifier.height(16.dp))

            PerfilItem("Alias", alias, Icons.Filled.Badge) { onNavigateToEditPerfil("alias") }
            PerfilItem("Correo", correo, Icons.Filled.AlternateEmail) {}
            PerfilItem("Información", info, Icons.Filled.Info) { onNavigateToEditPerfil("info") }
            PerfilItem("Teléfono", telefono, Icons.Filled.Call) { onNavigateToEditPerfil("telefono") }
            PerfilItem("Género", genero, Icons.Filled.Person) { onNavigateToEditPerfil("genero") }
            PerfilItem("Fecha de nacimiento", fechaNacimiento, Icons.Filled.CalendarMonth) { onNavigateToEditPerfil("fecha_nacimiento") }
            PerfilItem("Provincia", provincia, Icons.Filled.LocationOn) { onNavigateToEditPerfil("provincia") }
        }
    }
}

@Composable
private fun PerfilItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(
        Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, Modifier.size(40.dp), tint = Color.DarkGray)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(label, fontSize = 14.sp, color = Color.Gray)
                Text(value.ifEmpty { "Desconocido" }, fontSize = 16.sp, color = Color.Black)
            }
            Icon(Icons.Filled.Edit, "Editar", Modifier.size(24.dp), tint = Color.Gray)
        }
    }
}
