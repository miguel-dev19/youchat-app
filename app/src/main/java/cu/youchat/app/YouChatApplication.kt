package cu.youchat.app

import android.app.Application
import android.content.Context
import android.os.Environment

class YouChatApplication : Application() {
    companion object {
        var context: Context? = null

        @JvmStatic var mark: Int = 0
        @JvmStatic var correo: String? = null
        @JvmStatic var pass: String? = null
        @JvmStatic var alias: String? = null
        @JvmStatic var ruta_img_perfil: String? = null

        var cant_seguidores: Int = 0
        var temaApp: Int = 0
        var puedeHacerCopiaSeguridad = false

        @JvmStatic val RUTA_IMAGENES_PERFIL = Environment.getExternalStorageDirectory().toString() + "/YouChat/.Imágenes de prefil/"
        @JvmStatic val RUTA_COPIA_BASE_DATOS = Environment.getExternalStorageDirectory().toString() + "/YouChat/"

        fun configuracion1() {}
        fun configuracion2() {}
        fun configuracion3() {}
    }
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
