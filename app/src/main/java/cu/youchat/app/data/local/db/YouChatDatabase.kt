package cu.youchat.app.data.local.db
import androidx.room.Database
import androidx.room.RoomDatabase
import cu.youchat.app.data.local.dao.ChatDao
import cu.youchat.app.data.local.dao.ContactoDao
import cu.youchat.app.data.local.dao.UsuarioDao
import cu.youchat.app.data.local.entity.ChatEntity
import cu.youchat.app.data.local.entity.ContactoEntity
import cu.youchat.app.data.local.entity.UsuarioEntity
@Database(entities = [ContactoEntity::class, UsuarioEntity::class, ChatEntity::class], version = 1, exportSchema = false)
abstract class YouChatDatabase : RoomDatabase() {
    abstract fun contactoDao(): ContactoDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun chatDao(): ChatDao
}
