package cu.youchat.app.data.local.entity
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "contactos")
data class ContactoEntity(
    @PrimaryKey @ColumnInfo(name = "correo") val correo: String,
    val alias: String = "",
    val nombre: String = "",
    @ColumnInfo(name = "tipo_contacto") val tipoContacto: Int = 1,
    @ColumnInfo(name = "version_contacto") val version: Int = 0,
    @ColumnInfo(name = "ruta_img") val rutaImg: String = "",
    val info: String = "",
    val telefono: String = "",
    val genero: String = "",
    val provincia: String = "",
    @ColumnInfo(name = "fecha_nacimiento") val fechaNacimiento: String = "",
    @ColumnInfo(name = "hora_ult_conexion") val ultHoraConex: String = "",
    @ColumnInfo(name = "fecha_ult_conexion") val ultFechaConex: String = "",
    @ColumnInfo(name = "usa_youchat") val usaYouchat: Boolean = false,
    val silenciado: Boolean = false,
    val bloqueado: Boolean = false,
    @ColumnInfo(name = "nombreOrdenar") val nombreOrdenar: String = "",
    @ColumnInfo(name = "cant_seguidores") val cantSeguidores: Int = 0
)
