package plat.lab1.composelab4.lab7.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.composelab4.lab7.viewmodels.SplashViewModel

@Composable
fun SplashScreen(
    onLogged: () -> Unit,
    onNotLogged: () -> Unit,
    viewModel: SplashViewModel = viewModel()
) {
    val isLogged by viewModel.isLogged.collectAsState()

    LaunchedEffect(isLogged) {
        when (isLogged) {
            true -> onLogged()
            false -> onNotLogged()
            null -> Unit // estado de leyendo DataStore
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}