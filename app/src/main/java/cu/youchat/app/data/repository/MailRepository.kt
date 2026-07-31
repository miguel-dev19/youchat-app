package cu.youchat.app.data.repository
import cu.youchat.app.data.local.dao.ChatDao
import cu.youchat.app.data.local.dao.UsuarioDao
import cu.youchat.app.data.local.entity.ChatEntity
import cu.youchat.app.data.local.entity.UsuarioEntity
import cu.youchat.app.data.preferences.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import com.sun.mail.imap.IMAPFolder
import com.sun.mail.imap.IMAPStore
import com.sun.mail.smtp.SMTPTransport
@Singleton
class MailRepository @Inject constructor(
    private val preferences: UserPreferences,
    private val chatDao: ChatDao,
    private val usuarioDao: UsuarioDao
) {
    private var imapStore: IMAPStore? = null
    private var smtpTransport: SMTPTransport? = null
    private var imapFolder: IMAPFolder? = null
    suspend fun conectarIMAP(): Boolean = withContext(Dispatchers.IO) {
        try {
            val correo = preferences.correo.first()
            val pass = preferences.pass.first()
            val props = Properties().apply {
                put("mail.store.protocol", "imap")
                put("mail.imap.host", "imap.nauta.cu")
                put("mail.imap.port", "143")
                put("mail.imap.starttls.enable", "false")
            }
            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(correo, pass)
            })
            imapStore = session.getStore("imap") as IMAPStore
            imapStore?.connect("imap.nauta.cu", correo, pass)
            imapFolder = imapStore?.getFolder("Inbox") as? IMAPFolder
            imapFolder?.open(Folder.READ_WRITE)
            true
        } catch (e: Exception) { false }
    }
    suspend fun conectarSMTP(): Boolean = withContext(Dispatchers.IO) {
        try {
            val correo = preferences.correo.first()
            val pass = preferences.pass.first()
            val props = Properties().apply {
                put("mail.smtp.host", "smtp.nauta.cu")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", "25")
            }
            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(correo, pass)
            })
            smtpTransport = SMTPTransport(session, javax.mail.URLName("smtp", "smtp.nauta.cu", 25, null, correo, pass))
            if (!smtpTransport!!.isConnected) smtpTransport?.connect()
            true
        } catch (e: Exception) { false }
    }
    suspend fun verificarCredenciales(correo: String, pass: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val props = Properties().apply {
                put("mail.smtp.host", "smtp.nauta.cu")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", "25")
            }
            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(correo, pass)
            })
            val transport = SMTPTransport(session, javax.mail.URLName("smtp", "smtp.nauta.cu", 25, null, correo, pass))
            if (!transport.isConnected) transport.connect()
            transport.isConnected
        } catch (e: Exception) { false }
    }
    suspend fun buscarMensajesYouChat() = withContext(Dispatchers.IO) {
        try {
            if (imapFolder == null || !imapFolder!!.isOpen) {
                imapFolder = imapStore?.getFolder("Inbox") as? IMAPFolder
                imapFolder?.open(Folder.READ_WRITE)
            }
            val asunto = SubjectTerm("YouChat")
            val messages = imapFolder?.search(asunto) ?: return@withContext
            messages.forEach { message ->
                val from = message.from?.firstOrNull()?.toString()?.trim() ?: return@forEach
                val headers = message.allHeaders
                val idHeader = message.getHeader("msg_id")?.firstOrNull() ?: return@forEach
                val tipoHeader = message.getHeader("msg_tipo")?.firstOrNull() ?: "1"
                val tipoMensaje = try { tipoHeader.toInt() } catch (e: Exception) { 1 }
                val idMsgResp = message.getHeader("msg_id_resp")?.firstOrNull() ?: ""
                val contenido = message.content?.toString()?.trim() ?: ""
                val orden = java.text.SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault()).format(Date())
                val hora = java.text.SimpleDateFormat("yyyyMMddHHmmss").format(message.sentDate ?: Date())
                val fecha = java.text.SimpleDateFormat("dd/MM/yyyy").format(message.sentDate ?: Date())
                val chatEntity = ChatEntity(
                    id = idHeader,
                    tipoMensaje = tipoMensaje,
                    estado = 4,
                    correo = from,
                    mensaje = contenido,
                    idMsgResp = idMsgResp,
                    correoEmisor = from,
                    orden = orden,
                    hora = hora,
                    fecha = fecha
                )
                chatDao.insertarMensaje(chatEntity)
                val usuario = usuarioDao.getUsuario(from)
                if (usuario != null) {
                    usuarioDao.incrementarMsgNoVistos(from)
                } else {
                    usuarioDao.insertarUsuario(UsuarioEntity(correo = from, ultMsgTipo = tipoMensaje, ultMsgTexto = contenido, ultMsgOrden = orden))
                }
                message.setFlag(Flags.Flag.DELETED, true)
            }
            imapFolder?.expunge()
        } catch (e: Exception) { e.printStackTrace() }
    }
    suspend fun enviarMensajeChat(destinatario: String, asunto: String, mensaje: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val correo = preferences.correo.first()
            val pass = preferences.pass.first()
            val props = Properties().apply {
                put("mail.smtp.host", "smtp.nauta.cu")
                put("mail.smtp.auth", "true")
                put("mail.smtp.port", "25")
            }
            val session = Session.getDefaultInstance(props, object : Authenticator() {
                override fun getPasswordAuthentication() = PasswordAuthentication(correo, pass)
            })
            val message = MimeMessage(session).apply {
                setFrom(InternetAddress(correo))
                addRecipient(Message.RecipientType.TO, InternetAddress(destinatario))
                this.subject = asunto
                setText(mensaje)
            }
            Transport.send(message)
            true
        } catch (e: Exception) { false }
    }
    fun desconectar() {
        try { imapFolder?.close(false) } catch (e: Exception) {}
        try { imapStore?.close() } catch (e: Exception) {}
        try { smtpTransport?.close() } catch (e: Exception) {}
    }
}
