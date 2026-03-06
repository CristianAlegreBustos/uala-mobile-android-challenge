package com.example.mobilechallenge_uala.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge_uala.domain.City

@Composable
fun MainScreen(
    uiState: UiState,
    onSearchQueryChanged: (String) -> Unit,
    onToggleFavoriteFilter: () -> Unit,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit,
    onFavoriteToggle: (City) -> Unit
) {
    // Detectar la orientación del dispositivo
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Modo Horizontal: Lista a la izquierda, Mapa a la derecha
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(1f)) {
                CityListContent(
                    uiState = uiState,
                    onSearchQueryChanged = onSearchQueryChanged,
                    onToggleFavoriteFilter = onToggleFavoriteFilter,
                    onCityClick = onCityClick,
                    onInfoClick = onInfoClick,
                    onFavoriteToggle = onFavoriteToggle
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                Text("Mapa interactivo aquí", modifier = Modifier.align(Alignment.Center))
            }
        }
    } else {
        // Modo Vertical
        CityListContent(
            uiState = uiState,
            onSearchQueryChanged = onSearchQueryChanged,
            onToggleFavoriteFilter = onToggleFavoriteFilter,
            onCityClick = onCityClick,
            onInfoClick = onInfoClick,
            onFavoriteToggle = onFavoriteToggle
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityListContent(
    uiState: UiState,
    onSearchQueryChanged: (String) -> Unit,
    onToggleFavoriteFilter: () -> Unit,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit,
    onFavoriteToggle: (City) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChanged,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Buscar ciudad por prefijo...") },
                shape = RoundedCornerShape(24.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF005CEE),
                    unfocusedBorderColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = uiState.showOnlyFavorites,
                onClick = onToggleFavoriteFilter,
                label = { Text("Favs") }
            )
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF005CEE))
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = uiState.displayedCities, key = { it.id }) { city ->
                    CityListItem(
                        city = city,
                        isFavorite = uiState.favoriteIds.contains(city.id),
                        onCityClick = onCityClick,
                        onInfoClick = onInfoClick,
                        onFavoriteToggle = onFavoriteToggle
                    )
                }
            }
        }
    }
}