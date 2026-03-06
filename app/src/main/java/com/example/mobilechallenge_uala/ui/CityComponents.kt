package com.example.mobilechallenge_uala.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge_uala.domain.City
import com.example.mobilechallenge_uala.ui.theme.UalaBlue

@Composable
fun CityListItem(
    city: City,
    isFavorite: Boolean,
    onCityClick: (City) -> Unit,
    onInfoClick: (City) -> Unit,
    onFavoriteToggle: (City) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onCityClick(city) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${city.name}, ${city.country}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D1D1D)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Lat: ${city.coordinates.lat}, Lon: ${city.coordinates.lon}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            IconButton(onClick = { onInfoClick(city) }) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = UalaBlue)
            }
            IconButton(onClick = { onFavoriteToggle(city) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFavorite) UalaBlue else Color.LightGray // Corazón Azul Ualá
                )
            }
        }
    }
}