package cu.youchat.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cu.youchat.app.YouChatApplication
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    onNavigateToChat: (correo: String, nombre: String) -> Unit = { _, _ -> },
    onNavigateToPerfil: () -> Unit = {},
    onNavigateToContactos: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    val alias = YouChatApplication.alias ?: YouChatApplication.correo ?: "Usuario"
    val correo = YouChatApplication.correo ?: ""
    val rutaPerfil = YouChatApplication.ruta_img_perfil ?: ""
    val cantSeguidores = YouChatApplication.cant_seguidores

    val chats = remember {
        listOf(
            ChatItem("usuario1@nauta.cu", "Usuario 1", "Último mensaje...", "10:30", 2, ""),
            ChatItem("usuario2@nauta.cu", "Usuario 2", "Hola, ¿cómo estás?", "09:15", 0, ""),
            ChatItem("usuario3@nauta.cu", "Usuario 3", "Archivo", "Ayer", 1, ""),
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(300.dp),
                drawerContainerColor = Color.White
            ) {
                Box(Modifier.fillMaxWidth().height(170.dp)) {
                    Box(Modifier.fillMaxSize().background(Color(0xFF3F51B5)))
                    Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.25f)))
                    Row(
                        Modifier.fillMaxWidth().padding(top = 23.dp, start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (rutaPerfil.isNotEmpty() && File(rutaPerfil).exists()) {
                            AsyncImage(
                                model = ImageRequest.Builder(ctx).data(rutaPerfil).crossfade(true).build(),
                                contentDescription = "Foto",
                                modifier = Modifier.size(75.dp).clip(CircleShape)
                            )
                        } else {
                            Icon(Icons.Filled.AccountCircle, "Foto", Modifier.size(75.dp), tint = Color.White)
                        }
                        Column(Modifier.padding(start = 8.dp)) {
                            Text(alias, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                            Text(correo, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            Spacer(Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Star, null, Modifier.size(16.dp), tint = Color.White)
                                Text("$cantSeguidores seguidores", color = Color.White, fontSize = 16.sp)
                            }
                        }
                    }
                }

                ListItem(
                    headlineContent = { Text("Mi perfil", fontSize = 16.sp) },
                    leadingContent = { Icon(Icons.Filled.Person, null, tint = Color.DarkGray) },
                    modifier = Modifier.clickable {
                        scope.launch { drawerState.close() }
                        onNavigateToPerfil()
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("YouChat", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, "Menú")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF3F51B5),
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToContactos() },
                    containerColor = Color(0xFF3F51B5)
                ) { Icon(Icons.Filled.Chat, "Nuevo chat", tint = Color.White) }
            }
        ) { padding ->
            LazyColumn(
                Modifier.fillMaxSize().padding(padding).background(Color.White)
            ) {
                items(chats) { chat ->
                    Surface(
                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToChat(chat.correo, chat.nombre) },
                        color = Color.White
                    ) {
                        Row(
                            Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Filled.AccountCircle, null, Modifier.size(56.dp).clip(CircleShape), tint = Color.Gray)
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Text(chat.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(chat.ultimoMensaje, fontSize = 14.sp, color = Color.Gray)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(chat.hora, fontSize = 12.sp, color = Color.Gray)
                                if (chat.noLeidos > 0) {
                                    Badge(containerColor = Color(0xFF3F51B5), contentColor = Color.White) {
                                        Text("${chat.noLeidos}")
                                    }
                                }
                            }
                        }
                    }
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }
    }
}

data class ChatItem(
    val correo: String, val nombre: String, val ultimoMensaje: String,
    val hora: String, val noLeidos: Int, val rutaImagen: String
)
