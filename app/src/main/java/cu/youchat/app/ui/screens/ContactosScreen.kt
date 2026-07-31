package cu.youchat.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactosScreen(
    onBack: () -> Unit = {},
    onContactoSeleccionado: (correo: String, nombre: String) -> Unit = { _, _ -> },
    onNavigateToSeguirA: () -> Unit = {},
    onNavigateToNuevaConversacion: () -> Unit = {},
    onNavigateToNuevoContacto: () -> Unit = {},
    onNavigateToInvitarAmigos: () -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var ordenarPorNombre by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current

    // Datos de contactos simulados
    val contactos = remember {
        listOf(
            ContactoItem("Usuario 1", "usuario1@nauta.cu", "Disponible", "ruta_foto_1"),
            ContactoItem("Usuario 2", "usuario2@nauta.cu", "Ocupado", ""),
            ContactoItem("Usuario 3", "usuario3@nauta.cu", "En línea", "ruta_foto_3"),
            ContactoItem("Amigo 4", "amigo4@nauta.cu", "", ""),
            ContactoItem("Amigo 5", "amigo5@nauta.cu", "Hola!", ""),
        )
    }

    val contactosFiltrados = if (searchText.isBlank()) contactos
        else contactos.filter { it.nombre.contains(searchText, true) || it.correo.contains(searchText, true) }

    Scaffold(
        topBar = {
            AnimatedContent(targetState = showSearch) { buscando ->
                if (buscando) {
                    // Barra de búsqueda
                    TopAppBar(
                        title = {
                            OutlinedTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = { Text("Buscar...", color = Color.White.copy(alpha = 0.7f)) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    cursorColor = Color.White
                                ),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                showSearch = false
                                searchText = ""
                            }) {
                                Icon(Icons.Filled.ArrowBack, "Cerrar", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF3F51B5)
                        )
                    )
                } else {
                    // Barra normal
                    TopAppBar(
                        title = {
                            Column {
                                Text("Nueva conversación", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                                Text("${contactos.size} Contactos", fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Filled.ArrowBack, "Atrás", tint = Color.White)
                            }
                        },
                        actions = {
                            IconButton(onClick = { showMenu = !showMenu }) {
                                Icon(Icons.Filled.MoreVert, "Opciones", tint = Color.White)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF3F51B5)
                        )
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).background(Color.White)) {

            LazyColumn(Modifier.fillMaxSize()) {
                items(contactosFiltrados) { contacto ->
                    ContactoRow(
                        contacto = contacto,
                        onClick = { onContactoSeleccionado(contacto.correo, contacto.nombre) }
                    )
                }
            }

            // Texto "sin contactos"
            if (contactosFiltrados.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no hay contactos", fontSize = 18.sp, color = Color.Gray)
                }
            }

            // Menú desplegable
            AnimatedVisibility(
                visible = showMenu,
                enter = fadeIn() + scaleIn(transformOrigin = TransformOrigin(1f, 0f)),
                exit = fadeOut() + scaleOut(transformOrigin = TransformOrigin(1f, 0f)),
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Card(
                    modifier = Modifier
                        .padding(top = 4.dp, end = 6.dp)
                        .width(220.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column {
                        MenuItem("Buscar", Icons.Filled.Search) {
                            showMenu = false; showSearch = true
                        }
                        MenuItem("Actualizar lista", Icons.Filled.Refresh) {
                            showMenu = false
                        }
                        MenuItem("Seguir a", Icons.Filled.Star) {
                            showMenu = false; onNavigateToSeguirA()
                        }
                        MenuItem("Nueva conversación", Icons.Filled.Chat) {
                            showMenu = false; onNavigateToNuevaConversacion()
                        }
                        MenuItem("Nuevo contacto", Icons.Filled.PersonAdd) {
                            showMenu = false; onNavigateToNuevoContacto()
                        }
                        MenuItem("Invitar amigos", Icons.Filled.Share) {
                            showMenu = false; onNavigateToInvitarAmigos()
                        }
                        MenuItem(
                            if (ordenarPorNombre) "Ordenar por correo" else "Ordenar por nombre",
                            Icons.Filled.Sort
                        ) {
                            ordenarPorNombre = !ordenarPorNombre; showMenu = false
                        }
                    }
                }
            }

            // Fondo oscuro al mostrar menú
            if (showMenu) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable { showMenu = false }
                )
            }
        }
    }
}

data class ContactoItem(
    val nombre: String,
    val correo: String,
    val info: String,
    val rutaImagen: String
)

@Composable
private fun ContactoRow(contacto: ContactoItem, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Foto
            Icon(
                Icons.Filled.AccountCircle,
                null,
                Modifier.size(48.dp).clip(CircleShape),
                tint = Color(0xFFBDBDBD)
            )

            Spacer(Modifier.width(16.dp))

            // Nombre y estado
            Column(Modifier.weight(1f)) {
                Text(
                    contacto.nombre,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (contacto.info.isNotEmpty()) {
                    Text(
                        contacto.info,
                        fontSize = 13.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Indicador de YouChat
            Icon(
                Icons.Filled.Circle,
                "YouChat",
                Modifier.size(10.dp),
                tint = Color(0xFF3F51B5)
            )
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
    }
}

@Composable
private fun MenuItem(texto: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, Modifier.size(22.dp), tint = Color.DarkGray)
        Spacer(Modifier.width(12.dp))
        Text(texto, fontSize = 15.sp, color = Color.DarkGray)
    }
}

@Composable
private fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: androidx.compose.ui.unit.Dp = 1.dp,
    color: Color = Color.LightGray
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}
