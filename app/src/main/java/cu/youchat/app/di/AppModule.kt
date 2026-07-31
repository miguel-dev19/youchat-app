package cu.youchat.app.di
import android.content.Context
import androidx.room.Room
import cu.youchat.app.data.local.db.YouChatDatabase
import cu.youchat.app.data.local.dao.ChatDao
import cu.youchat.app.data.local.dao.ContactoDao
import cu.youchat.app.data.local.dao.UsuarioDao
import cu.youchat.app.data.preferences.UserPreferences
import cu.youchat.app.data.repository.ChatRepository
import cu.youchat.app.data.repository.MailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): YouChatDatabase {
        return Room.databaseBuilder(context, YouChatDatabase::class.java, "bd_youchat").build()
    }
    @Provides fun provideContactoDao(db: YouChatDatabase): ContactoDao = db.contactoDao()
    @Provides fun provideUsuarioDao(db: YouChatDatabase): UsuarioDao = db.usuarioDao()
    @Provides fun provideChatDao(db: YouChatDatabase): ChatDao = db.chatDao()
    @Provides @Singleton fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences = UserPreferences(context)
    @Provides @Singleton fun provideChatRepository(usuarioDao: UsuarioDao, chatDao: ChatDao, contactoDao: ContactoDao): ChatRepository = ChatRepository(usuarioDao, chatDao, contactoDao)
    @Provides @Singleton fun provideMailRepository(preferences: UserPreferences, chatDao: ChatDao, usuarioDao: UsuarioDao): MailRepository = MailRepository(preferences, chatDao, usuarioDao)
}
