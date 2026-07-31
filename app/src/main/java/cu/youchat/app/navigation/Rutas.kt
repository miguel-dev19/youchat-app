package cu.youchat.app.navigation
object Rutas {
    const val ONBOARDING = "onboarding"
    const val LOGIN = "login"
    const val WELCOME_PERFIL = "welcome_perfil"
    const val PRINCIPAL = "principal"
    const val VIEW_YOU_PERFIL = "view_you_perfil"
    const val CONTACTOS = "contactos"
    const val EDIT_PERFIL = "edit_perfil/{campo}"
    const val CHAT = "chat/{correo}/{nombre}"
    fun editPerfil(campo: String) = "edit_perfil/$campo"
    fun chat(correo: String, nombre: String) = "chat/$correo/$nombre"
}
