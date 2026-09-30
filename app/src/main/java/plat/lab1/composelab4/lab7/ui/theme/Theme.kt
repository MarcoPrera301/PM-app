package plat.lab1.composelab4.lab7.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//materialtheme

private val RmPrimary = Color(0xFF3B7A57)
private val RmPrimaryDark = Color(0xFF1F4A32)
private val RmBackground = Color(0xFFF7F7F5)

private val RickMortyColorScheme = lightColorScheme(
    primary = RmPrimary,
    onPrimary = Color.White,
    secondary = RmPrimaryDark,
    onSecondary = Color.White,
    background = RmBackground,
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A)
)

@Composable
fun RickMortyTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = RickMortyColorScheme, content = content)
}