package cu.youchat.app.domain.model
data class ChatPreview(
    val correo: String, val nombre: String = "", val rutaImg: String = "",
    val estaAnclado: Boolean = false, val mensajesNoLeidos: Int = 0,
    val ultimoMensaje: String = "", val ultimoMensajeTipo: Int = 0,
    val ultimaHora: String = "", val ultimaFecha: String = "",
    val estaEnLinea: Boolean = false, val usaYouchat: Boolean = false
)
