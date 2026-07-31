package cu.youchat.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF3F51B5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDBE1FF),
    secondary = Color(0xFF03DAC5),
    background = Color.White,
    surface = Color.White,
    error = Color(0xFFB00020),
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun YouChatTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
