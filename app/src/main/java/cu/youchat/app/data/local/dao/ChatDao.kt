package cu.youchat.app.data.local.dao
import androidx.room.*
import cu.youchat.app.data.local.entity.ChatEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE correo = :correo ORDER BY orden DESC LIMIT :limite")
    fun getMensajes(correo: String, limite: Int = 50): Flow<List<ChatEntity>>
    @Query("SELECT * FROM chats WHERE correo = :correo ORDER BY orden DESC LIMIT 1")
    suspend fun getUltimoMensaje(correo: String): ChatEntity?
    @Query("SELECT * FROM chats WHERE id = :id LIMIT 1")
    suspend fun getMensaje(id: String): ChatEntity?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMensaje(chat: ChatEntity)
    @Query("UPDATE chats SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: String, estado: Int)
    @Query("UPDATE chats SET mensaje = :texto, editado = 1 WHERE id = :id")
    suspend fun editarMensaje(id: String, texto: String)
    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun eliminarMensaje(id: String)
    @Query("SELECT * FROM chats WHERE estado IN (1, 2) ORDER BY orden ASC")
    suspend fun getMensajesPendientes(): List<ChatEntity>
    @Query("SELECT * FROM chats WHERE id = :id AND esta_descargado = 0 LIMIT 1")
    suspend fun getMensajeNoDescargado(id: String): ChatEntity?
    @Query("UPDATE chats SET ruta_dato = :ruta, esta_descargado = 1 WHERE id = :id")
    suspend fun marcarComoDescargado(id: String, ruta: String)
}
