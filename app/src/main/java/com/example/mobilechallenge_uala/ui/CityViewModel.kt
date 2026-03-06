package com.example.mobilechallenge_uala.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilechallenge_uala.data.CityRepository
import com.example.mobilechallenge_uala.domain.City
import com.example.mobilechallenge_uala.domain.CitySearchEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class UiState(
    val isLoading: Boolean = true,
    val searchQuery: String = "",
    val showOnlyFavorites: Boolean = false,
    val allCities: List<City> = emptyList(),
    val displayedCities: List<City> = emptyList(),
    val favoriteIds: Set<Int> = emptySet()
)

class CityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = CityRepository()
    private val searchEngine = CitySearchEngine()
    private val prefs = application.getSharedPreferences("uala_favorites", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init { loadCities() }

    private fun loadCities() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val rawCities = repository.fetchCities()

            //  IDs de favoritos guardados
            val savedFavorites = prefs.getStringSet("favorites", emptySet())?.map { it.toInt() }?.toSet() ?: emptySet()

            searchEngine.insertCities(rawCities)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                allCities = rawCities,
                displayedCities = rawCities,
                favoriteIds = savedFavorites
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        applyFilters()
    }

    fun onToggleFavoriteFilter() {
        _uiState.value = _uiState.value.copy(showOnlyFavorites = !_uiState.value.showOnlyFavorites)
        applyFilters()
    }

    fun toggleCityFavorite(city: City) {
        val currentFavs = _uiState.value.favoriteIds.toMutableSet()
        if (currentFavs.contains(city.id)) {
            currentFavs.remove(city.id)
        } else {
            currentFavs.add(city.id)
        }

        // persistencia
        prefs.edit().putStringSet("favorites", currentFavs.map { it.toString() }.toSet()).apply()

        // Actualizar el estado para que Compose redibuje el corazón
        _uiState.value = _uiState.value.copy(favoriteIds = currentFavs)
        applyFilters()
    }

    private fun applyFilters() {
        val state = _uiState.value
        var filteredList = if (state.searchQuery.isBlank()) state.allCities else searchEngine.search(state.searchQuery)

        if (state.showOnlyFavorites) {
            filteredList = filteredList.filter { state.favoriteIds.contains(it.id) }
        }
        _uiState.value = state.copy(displayedCities = filteredList)
    }
}