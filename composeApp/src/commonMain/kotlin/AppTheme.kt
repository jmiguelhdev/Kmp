import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Definición de colores personalizados
private val PrimaryColor = Color(0xFF000000) // Negro
private val BackgroundLight = Color(0xFFF5F5F5)
private val BackgroundDark = Color(0xFF121212)

// Paleta para Modo Claro
private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = Color.White,
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = Color.White, // Claro en modo claro
    onSurface = Color.Black // Texto oscuro sobre fondo claro
)

// Paleta para Modo Oscuro
private val DarkColors = darkColorScheme(
    primary = Color.White, // Blanco en modo oscuro para contraste si se usa como primary
    onPrimary = Color.Black,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E), // Oscuro en modo oscuro
    onSurface = Color.White // Texto claro sobre fondo oscuro
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes().copy(
            small = AbsoluteCutCornerShape(0.dp),
            medium = AbsoluteCutCornerShape(0.dp),
            large = AbsoluteCutCornerShape(0.dp)
        ),
        content = content
    )
}
