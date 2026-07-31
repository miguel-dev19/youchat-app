package cu.youchat.app.domain.model
data class Contacto(
    val correo: String, val alias: String = "", val nombre: String = "",
    val tipoContacto: Int = 1, val version: Int = 0, val rutaImg: String = "",
    val info: String = "", val telefono: String = "", val genero: String = "",
    val provincia: String = "", val fechaNacimiento: String = "",
    val ultHoraConex: String = "", val ultFechaConex: String = "",
    val usaYouchat: Boolean = false, val silenciado: Boolean = false,
    val bloqueado: Boolean = false, val cantSeguidores: Int = 0
) {
    val nombreMostrar: String get() = when {
        nombre.isNotBlank() && nombre != correo -> nombre
        alias.isNotBlank() -> alias
        else -> correo
    }
}
