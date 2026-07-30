package cu.youchat.app

import android.app.Application
import android.content.Context
import android.os.Environment

class YouChatApplication : Application() {
    companion object {
        var context: Context? = null
        var mark: Int = 0
        var correo: String? = null
        var pass: String? = null
        var alias: String? = null
        var info: String? = null
        var telefono: String? = null
        var genero: String? = null
        var provincia: String? = null
        var fecha_nacimiento: String? = null
        var ruta_img_perfil: String? = null
        var ruta_fondo_chat: String? = null
        var cant_seguidores: Int = 0
        var temaApp: Int = 0
        var puedeHacerCopiaSeguridad = false
        val RUTA_IMAGENES_PERFIL = Environment.getExternalStorageDirectory().toString() + "/YouChat/.Imágenes de prefil/"
        val RUTA_COPIA_BASE_DATOS = Environment.getExternalStorageDirectory().toString() + "/YouChat/"
        fun configuracion1() {}
        fun configuracion2() {}
        fun configuracion3() {}
    }
    override fun onCreate() { super.onCreate(); context = this }
}
