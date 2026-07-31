package cu.youchat.app.domain.model
data class Mensaje(
    val id: String, val tipoMensaje: Int, val estado: Int, val correo: String,
    val mensaje: String = "", val rutaDato: String = "", val hora: String = "",
    val fecha: String = "", val idMsgResp: String = "", val emisor: String = "",
    val esReenviado: Boolean = false, val orden: String = "",
    val esEditado: Boolean = false, val descargado: Boolean = true, val peso: Int = 0
) {
    val esMio: Boolean get() = tipoMensaje % 2 == 0
    val esImagen: Boolean get() = tipoMensaje in listOf(3, 4)
    val esAudio: Boolean get() = tipoMensaje in listOf(7, 8, 9, 10)
    val esSticker: Boolean get() = tipoMensaje in listOf(19, 20)
    val esArchivo: Boolean get() = tipoMensaje in listOf(13, 14)
    val previewTexto: String get() = when {
        esImagen -> "🖼 Imagen"
        esAudio -> "🎤 Audio"
        esSticker -> "🎯 Sticker"
        esArchivo -> "📎 Archivo"
        else -> mensaje
    }
}
