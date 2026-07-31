package cu.youchat.app.data.local.dao
import androidx.room.*
import cu.youchat.app.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios ORDER BY anclado DESC, ult_msg_orden DESC")
    fun getUsuariosOrdenados(): Flow<List<UsuarioEntity>>
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun getUsuario(correo: String): UsuarioEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuario(usuario: UsuarioEntity)
    @Query("UPDATE usuarios SET cant_msg = 0 WHERE correo = :correo")
    suspend fun marcarComoVisto(correo: String)
    @Query("UPDATE usuarios SET cant_msg = cant_msg + :cant WHERE correo = :correo")
    suspend fun incrementarMsgNoVistos(correo: String, cant: Int = 1)
    @Query("UPDATE usuarios SET anclado = :anclado WHERE correo = :correo")
    suspend fun actualizarAnclado(correo: String, anclado: Boolean)
    @Query("UPDATE usuarios SET borrador_usuario = :borrador WHERE correo = :correo")
    suspend fun guardarBorrador(correo: String, borrador: String)
    @Query("DELETE FROM usuarios WHERE correo = :correo")
    suspend fun eliminarUsuario(correo: String)
}
