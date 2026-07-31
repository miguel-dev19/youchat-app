package cu.youchat.app.data.local.dao
import androidx.room.*
import cu.youchat.app.data.local.entity.ContactoEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface ContactoDao {
    @Query("SELECT * FROM contactos WHERE tipo_contacto = 1 ORDER BY nombreOrdenar ASC")
    fun getContactosVisibles(): Flow<List<ContactoEntity>>
    @Query("SELECT * FROM contactos WHERE correo = :correo LIMIT 1")
    suspend fun getContacto(correo: String): ContactoEntity?
    @Query("SELECT * FROM contactos WHERE bloqueado = 1")
    fun getContactosBloqueados(): Flow<List<ContactoEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarContacto(contacto: ContactoEntity)
    @Update
    suspend fun actualizarContacto(contacto: ContactoEntity)
    @Query("DELETE FROM contactos WHERE correo = :correo")
    suspend fun eliminarContacto(correo: String)
    @Query("SELECT * FROM contactos WHERE correo = :correo AND tipo_contacto = 1 LIMIT 1")
    suspend fun existeContactoVisible(correo: String): ContactoEntity?
}
