package plat.lab1.composelab4.lab7.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab1.composelab4.lab7.common.ErrorView
import plat.lab1.composelab4.lab7.common.LoadingView
import plat.lab1.composelab4.lab7.data.Location
import plat.lab1.composelab4.lab7.viewmodels.LocationsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(onLocationClick: (Int) -> Unit,viewModel: LocationsListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                uiState.hasError -> {
                    ErrorView(
                        message = "Error al obtener listado de ubicaciones.\nIntenta de nuevo",
                        onRetryClick = { viewModel.loadLocations() }
                    )
                }
                uiState.isLoading -> {
                    LoadingView(onClick = { viewModel.onLoadingClicked() })
                }
                else -> {
                    val locations = uiState.data.orEmpty()
                    LazyColumn {
                        items(locations) { location ->
                            LocationRow(
                                location = location,
                                onClick = { onLocationClick(location.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationRow(location: Location, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(text = location.name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = location.type,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}