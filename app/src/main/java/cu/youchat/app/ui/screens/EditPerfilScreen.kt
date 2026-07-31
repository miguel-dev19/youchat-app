package cu.youchat.app.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPerfilScreen(campo: String, onBack: () -> Unit = {}) {
    val context = LocalContext.current
    var valor by remember { mutableStateOf("") }
    var fechaMostrada by remember { mutableStateOf("") }
    var generoSeleccionado by remember { mutableStateOf("") }
    var provinciaSeleccionada by remember { mutableStateOf("") }

    val titulo = when (campo) {
        "alias" -> "Editar alias"
        "info" -> "Editar información"
        "telefono" -> "Editar número"
        "genero" -> "Editar género"
        "fecha_nacimiento" -> "Editar fecha de nacimiento"
        "provincia" -> "Editar provincia"
        else -> "Editar"
    }

    val ayuda = when (campo) {
        "alias" -> "Este alias es el nombre público que van a ver los usuarios."
        "info" -> "Este campo es totalmente público."
        "telefono" -> "El número de teléfono expuesto acá será público."
        "genero" -> "Este campo es totalmente público."
        "fecha_nacimiento" -> "Este campo es totalmente público."
        "provincia" -> "Este campo es totalmente público."
        else -> ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo, fontWeight = FontWeight.Bold, fontSize = 19.sp) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Atrás") } },
                actions = { IconButton(onClick = onBack) { Icon(Icons.Filled.Check, "Guardar") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3F51B5), titleContentColor = Color.White, navigationIconContentColor = Color.White, actionIconContentColor = Color.White)
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).background(Color.White)) {
            Box(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                when (campo) {
                    "alias" -> {
                        OutlinedTextField(value = valor, onValueChange = { if (it.length <= 30) valor = it }, modifier = Modifier.fillMaxWidth().padding(16.dp), label = { Text("Alias") }, singleLine = true, leadingIcon = { Icon(Icons.Filled.Badge, null) })
                    }
                    "info" -> {
                        Column(Modifier.padding(16.dp)) {
                            OutlinedTextField(value = valor, onValueChange = { if (it.length <= 160) valor = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Acerca de usted...") }, minLines = 3, leadingIcon = { Icon(Icons.Filled.Info, null) })
                            Text("${valor.length}/160", fontSize = 13.sp, color = Color.Gray, modifier = Modifier.align(Alignment.End).padding(top = 4.dp))
                        }
                    }
                    "telefono" -> {
                        OutlinedTextField(value = valor, onValueChange = { valor = it }, modifier = Modifier.fillMaxWidth().padding(16.dp), label = { Text("Teléfono") }, singleLine = true, leadingIcon = { Icon(Icons.Filled.Call, null) })
                    }
                    "genero" -> {
                        Column(Modifier.padding(16.dp)) {
                            listOf("Masculino", "Femenino", "Ninguno").forEach { gen ->
                                Row(Modifier.fillMaxWidth().clickable { generoSeleccionado = gen }.padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(selected = generoSeleccionado == gen, onClick = { generoSeleccionado = gen })
                                    Spacer(Modifier.width(8.dp))
                                    Text(gen, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                    "fecha_nacimiento" -> {
                        Column(Modifier.fillMaxWidth().clickable {
                            val cal = Calendar.getInstance()
                            DatePickerDialog(context, { _, year, month, day ->
                                val meses = listOf("enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre")
                                fechaMostrada = "$day de ${meses[month]} de $year"
                            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                        }.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Filled.CalendarMonth, null, Modifier.size(50.dp), tint = Color.DarkGray)
                            Spacer(Modifier.height(8.dp))
                            Text(fechaMostrada.ifEmpty { "Fecha de nacimiento" }, fontSize = 16.sp, color = if (fechaMostrada.isEmpty()) Color.Gray else Color.Black)
                        }
                    }
                    "provincia" -> {
                        Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                            val provincias = listOf("Ninguna", "Pinar del Río", "Artemisa", "Mayabeque", "La Habana", "Matanzas", "Cienfuegos", "Villa Clara", "Sancti Spíritus", "Ciego de Ávila", "Camagüey", "Las Tunas", "Holguín", "Granma", "Santiago de Cuba", "Guantánamo", "Isla de la Juventud")
                            provincias.forEach { prov ->
                                Row(Modifier.fillMaxWidth().clickable { provinciaSeleccionada = prov }.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(selected = provinciaSeleccionada == prov, onClick = { provinciaSeleccionada = prov })
                                    Spacer(Modifier.width(8.dp))
                                    Text(prov, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }
            }
            Surface(Modifier.fillMaxWidth(), color = Color(0xFFF5F5F5)) {
                Text(ayuda, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(16.dp))
            }
            if (campo in listOf("alias", "info", "telefono")) {
                TextButton(onClick = { valor = "" }, modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    Text("Eliminar", color = Color(0xFFE53935), fontSize = 20.sp)
                }
            }
        }
    }
}
