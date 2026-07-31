package cu.youchat.app.data.repository
import cu.youchat.app.data.local.dao.ChatDao
import cu.youchat.app.data.local.dao.ContactoDao
import cu.youchat.app.data.local.dao.UsuarioDao
import cu.youchat.app.data.local.entity.ChatEntity
import cu.youchat.app.data.local.entity.ContactoEntity
import cu.youchat.app.data.local.entity.UsuarioEntity
import cu.youchat.app.domain.model.ChatPreview
import cu.youchat.app.domain.model.Contacto
import cu.youchat.app.domain.model.Mensaje
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class ChatRepository @Inject constructor(
    private val usuarioDao: UsuarioDao,
    private val chatDao: ChatDao,
    private val contactoDao: ContactoDao
) {
    fun getChatsPreview(): Flow<List<ChatPreview>> {
        return usuarioDao.getUsuariosOrdenados().map { usuarios ->
            usuarios.map { usuario ->
                val contacto = contactoDao.getContacto(usuario.correo)
                ChatPreview(
                    correo = usuario.correo,
                    nombre = contacto?.nombreMostrar ?: usuario.correo,
                    rutaImg = contacto?.rutaImg ?: "",
                    estaAnclado = usuario.anclado,
                    mensajesNoLeidos = usuario.cantMsg,
                    ultimoMensaje = usuario.ultMsgTexto,
                    ultimoMensajeTipo = usuario.ultMsgTipo,
                    ultimaHora = "",
                    estaEnLinea = false,
                    usaYouchat = contacto?.usaYouchat ?: false
                )
            }
        }
    }
    fun getMensajes(correo: String): Flow<List<Mensaje>> {
        return chatDao.getMensajes(correo).map { list ->
            list.map { it.toDomain() }
        }
    }
    suspend fun insertarMensaje(mensaje: Mensaje) {
        chatDao.insertarMensaje(mensaje.toEntity())
        val usuario = usuarioDao.getUsuario(mensaje.correo)
        if (usuario != null) {
            usuarioDao.incrementarMsgNoVistos(mensaje.correo)
        } else {
            usuarioDao.insertarUsuario(
                UsuarioEntity(
                    correo = mensaje.correo,
                    ultMsgTipo = mensaje.tipoMensaje,
                    ultMsgTexto = mensaje.previewTexto,
                    ultMsgOrden = mensaje.orden
                )
            )
        }
    }
    fun getContactos(): Flow<List<Contacto>> {
        return contactoDao.getContactosVisibles().map { list ->
            list.map { it.toDomain() }
        }
    }
    suspend fun getContacto(correo: String): Contacto? {
        return contactoDao.getContacto(correo)?.toDomain()
    }
    suspend fun insertarContacto(contacto: Contacto) {
        contactoDao.insertarContacto(contacto.toEntity())
    }
    private fun ChatEntity.toDomain() = Mensaje(
        id = id, tipoMensaje = tipoMensaje, estado = estado,
        correo = correo, mensaje = mensaje, rutaDato = rutaDato,
        hora = hora, fecha = fecha, idMsgResp = idMsgResp,
        emisor = correoEmisor, esReenviado = reenviado,
        orden = orden, esEditado = editado, descargado = estaDescargado, peso = peso
    )
    private fun Mensaje.toEntity() = ChatEntity(
        id = id, tipoMensaje = tipoMensaje, estado = estado,
        correo = correo, mensaje = mensaje, rutaDato = rutaDato,
        hora = hora, fecha = fecha, idMsgResp = idMsgResp,
        correoEmisor = emisor, reenviado = esReenviado,
        orden = orden, editado = esEditado, estaDescargado = descargado, peso = peso
    )
    private fun ContactoEntity.toDomain() = Contacto(
        correo = correo, alias = alias, nombre = nombre,
        tipoContacto = tipoContacto, version = version, rutaImg = rutaImg,
        info = info, telefono = telefono, genero = genero,
        provincia = provincia, fechaNacimiento = fechaNacimiento,
        ultHoraConex = ultHoraConex, ultFechaConex = ultFechaConex,
        usaYouchat = usaYouchat, silenciado = silenciado,
        bloqueado = bloqueado, cantSeguidores = cantSeguidores
    )
    private fun Contacto.toEntity() = ContactoEntity(
        correo = correo, alias = alias, nombre = nombre,
        tipoContacto = tipoContacto, version = version, rutaImg = rutaImg,
        info = info, telefono = telefono, genero = genero,
        provincia = provincia, fechaNacimiento = fechaNacimiento,
        ultHoraConex = ultHoraConex, ultFechaConex = ultFechaConex,
        usaYouchat = usaYouchat, silenciado = silenciado,
        bloqueado = bloqueado, nombreOrdenar = nombre.lowercase(),
        cantSeguidores = cantSeguidores
    )
}
