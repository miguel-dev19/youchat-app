package cu.youchat.app.data.repository

import android.util.Log
import cu.youchat.app.data.local.dao.ChatDao
import cu.youchat.app.data.local.dao.UsuarioDao
import cu.youchat.app.data.preferences.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Properties
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MailRepository @Inject constructor(
    private val preferences: UserPreferences,
    private val chatDao: ChatDao,
    private val usuarioDao: UsuarioDao
) {
    suspend fun verificarCredenciales(correo: String, pass: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val props = Properties()
            props["mail.smtp.host"] = "smtp.nauta.cu"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.port"] = "25"
            
            val session = javax.mail.Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
                override fun getPasswordAuthentication() = javax.mail.PasswordAuthentication(correo, pass)
            })
            val transport = session.getTransport("smtp")
            transport.connect("smtp.nauta.cu", correo, pass)
            val connected = transport.isConnected
            transport.close()
            connected
        } catch (e: Exception) {
            Log.e("MailRepository", "Error verificando: ${e.message}")
            false
        }
    }

    suspend fun enviarMensajeChat(destinatario: String, asunto: String, mensaje: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val correo = preferences.correo.first()
            val pass = preferences.pass.first()
            val props = Properties()
            props["mail.smtp.host"] = "smtp.nauta.cu"
            props["mail.smtp.auth"] = "true"
            props["mail.smtp.port"] = "25"
            
            val session = javax.mail.Session.getDefaultInstance(props, object : javax.mail.Authenticator() {
                override fun getPasswordAuthentication() = javax.mail.PasswordAuthentication(correo, pass)
            })
            val message = javax.mail.internet.MimeMessage(session)
            message.setFrom(javax.mail.internet.InternetAddress(correo))
            message.addRecipient(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress(destinatario))
            message.subject = asunto
            message.setText(mensaje)
            javax.mail.Transport.send(message)
            true
        } catch (e: Exception) {
            Log.e("MailRepository", "Error enviando: ${e.message}")
            false
        }
    }
}
