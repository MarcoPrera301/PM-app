package plat.lab1.composelab4.lab7.common

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage


@Composable
fun CharacterAvatar(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val fallbackPainter = rememberVectorPainter(Icons.Filled.Person)

    AsyncImage(
        model = imageUrl.ifBlank { null },
        contentDescription = contentDescription,
        placeholder = fallbackPainter,
        error = fallbackPainter,
        fallback = fallbackPainter,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(CircleShape)
    )
}
