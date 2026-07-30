package cu.alexgi.youchat

import android.app.Application
import android.content.Context

class YouChatApplication : Application() {
    companion object {
        var context: Context? = null
        var mark: Int = 0
        var correo: String? = null
        var pass: String? = null
    }
    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
