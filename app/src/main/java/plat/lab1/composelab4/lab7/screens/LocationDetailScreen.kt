package plat.lab1.composelab4.lab7.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.composelab4.lab7.common.ErrorView
import plat.lab1.composelab4.lab7.common.LoadingView
import plat.lab1.composelab4.lab7.viewmodels.LocationDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationDetailScreen(onBackClick: () -> Unit, viewModel: LocationDetailViewModel = viewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.hasError -> {
                    ErrorView(
                        message = "Error al obtener ubicación.\nIntenta de nuevo",
                        onRetryClick = { viewModel.loadLocation() }
                    )
                }

                uiState.isLoading -> {
                    LoadingView(onClick = { viewModel.onLoadingClicked() })
                }

                else -> {
                    val location = uiState.data
                    if (location != null) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = location.name,
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            DetailRow(label = "ID:", value = location.id.toString())
                            DetailRow(label = "Type:", value = location.type)
                            DetailRow(label = "Dimensions:", value = location.dimension)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(text = value, style = MaterialTheme.typography.bodyLarge)
    }
}