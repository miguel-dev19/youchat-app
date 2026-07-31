package cu.youchat.app.data.preferences
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "youchat_prefs")
class UserPreferences(private val context: Context) {
    companion object {
        val KEY_CORREO = stringPreferencesKey("correo")
        val KEY_PASS = stringPreferencesKey("pass")
        val KEY_ALIAS = stringPreferencesKey("alias")
        val KEY_INFO = stringPreferencesKey("info")
        val KEY_TELEFONO = stringPreferencesKey("telefono")
        val KEY_GENERO = stringPreferencesKey("genero")
        val KEY_PROVINCIA = stringPreferencesKey("provincia")
        val KEY_FECHA_NACIMIENTO = stringPreferencesKey("fecha_nacimiento")
        val KEY_RUTA_IMG_PERFIL = stringPreferencesKey("ruta_img_perfil")
        val KEY_RUTA_FONDO_CHAT = stringPreferencesKey("ruta_fondo_chat")
        val KEY_TEMA_APP = intPreferencesKey("tema_app")
        val KEY_CANT_SEGUIDORES = intPreferencesKey("cant_seguidores")
        val KEY_MARK = intPreferencesKey("mark")
        val KEY_SONIDO = booleanPreferencesKey("sonido")
        val KEY_NOTIFICACION = booleanPreferencesKey("notificacion")
        val KEY_LECTURA = booleanPreferencesKey("lectura")
        val KEY_ESTADO_PERSONAL = booleanPreferencesKey("estado_personal")
        val KEY_CHAT_SECURITY = booleanPreferencesKey("chat_security")
        val KEY_VERSION_INFO = intPreferencesKey("version_info")
    }
    val correo: Flow<String> = context.dataStore.data.map { it[KEY_CORREO] ?: "" }
    val pass: Flow<String> = context.dataStore.data.map { it[KEY_PASS] ?: "" }
    val alias: Flow<String> = context.dataStore.data.map { it[KEY_ALIAS] ?: "" }
    val temaApp: Flow<Int> = context.dataStore.data.map { it[KEY_TEMA_APP] ?: 0 }
    val mark: Flow<Int> = context.dataStore.data.map { it[KEY_MARK] ?: 0 }
    val sonido: Flow<Boolean> = context.dataStore.data.map { it[KEY_SONIDO] ?: true }
    val notificacion: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFICACION] ?: true }
    suspend fun setCorreo(correo: String) { context.dataStore.edit { it[KEY_CORREO] = correo } }
    suspend fun setPass(pass: String) { context.dataStore.edit { it[KEY_PASS] = pass } }
    suspend fun setAlias(alias: String) { context.dataStore.edit { it[KEY_ALIAS] = alias } }
    suspend fun setMark(mark: Int) { context.dataStore.edit { it[KEY_MARK] = mark } }
    suspend fun setTemaApp(tema: Int) { context.dataStore.edit { it[KEY_TEMA_APP] = tema } }
    suspend fun setSonido(sonido: Boolean) { context.dataStore.edit { it[KEY_SONIDO] = sonido } }
    suspend fun setNotificacion(noti: Boolean) { context.dataStore.edit { it[KEY_NOTIFICACION] = noti } }
    suspend fun guardarPerfil(alias: String, info: String, telefono: String, genero: String, provincia: String, fechaNac: String) {
        context.dataStore.edit {
            it[KEY_ALIAS] = alias
            it[KEY_INFO] = info
            it[KEY_TELEFONO] = telefono
            it[KEY_GENERO] = genero
            it[KEY_PROVINCIA] = provincia
            it[KEY_FECHA_NACIMIENTO] = fechaNac
        }
    }
    suspend fun clearAll() { context.dataStore.edit { it.clear() } }
}
