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
import javax.mail.internet.MimeMessage

@Singleton
class MailRepository @Inject constructor(
    private val preferences: UserPreferences,
    private val chatDao: ChatDao,
    private val usuarioDao: UsuarioDao
) {
    
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
            val transport = session.getTransport("smtp")
            transport.connect("smtp.nauta.cu", correo, pass)
            val connected = transport.isConnected
            transport.close()
            connected
        } catch (e: Exception) { false }
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
}
