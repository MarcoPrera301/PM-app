package plat.lab1.composelab4.lab7.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
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
import plat.lab1.composelab4.lab7.common.EmptyView
import plat.lab1.composelab4.lab7.common.ErrorView
import plat.lab1.composelab4.lab7.common.LoadingView
import plat.lab1.composelab4.lab7.common.OptionChips
import plat.lab1.composelab4.lab7.data.Location
import plat.lab1.composelab4.lab7.data.LocationFilter
import plat.lab1.composelab4.lab7.viewmodels.LocationsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(onLocationClick: (Int) -> Unit,viewModel: LocationsListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filter by viewModel.filter.collectAsState()

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
                    val visible = when (filter) {
                        LocationFilter.ALL -> locations
                        LocationFilter.FAVORITES -> locations.filter { it.isFavorite }
                        LocationFilter.NON_FAVORITES -> locations.filter { !it.isFavorite }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        OptionChips(
                            options = LocationFilter.entries,
                            selected = filter,
                            label = { it.label },
                            onSelect = { viewModel.setFilter(it) },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )

                        if (visible.isEmpty()) {
                            EmptyView(message = "No hay elementos a mostrar")
                        } else {
                            LazyColumn {
                                items(visible, key = { it.id }) { location ->
                                    LocationRow(
                                        location = location,
                                        onClick = { onLocationClick(location.id) },
                                        onFavoriteClick = { viewModel.toggleFavorite(location) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationRow(
    location: Location,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(vertical = 8.dp)) {
            Text(text = location.name, style = MaterialTheme.typography.titleMedium)
            Text(
                text = location.type,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (location.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Marcar como favorita",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}