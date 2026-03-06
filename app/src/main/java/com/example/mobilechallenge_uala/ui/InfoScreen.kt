package com.example.mobilechallenge_uala.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mobilechallenge_uala.domain.City

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(city: City, onBackClick: () -> Unit) {
    //  contexto para lanzar el navegador de internet
    val context = LocalContext.current

    // Uso Scaffold para estructurar la pantalla
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles de la Ciudad") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${city.name}, ${city.country}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Mostrar ID, que es el dato que no estaba en la lista
                    Text("ID de base de datos: ${city.id}", style = MaterialTheme.typography.bodyLarge)
                    Text("Latitud: ${city.coordinates.lat}", style = MaterialTheme.typography.bodyLarge)
                    Text("Longitud: ${city.coordinates.lon}", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (city.isFavorite) "🌟 Esta ciudad es una de tus favoritas" else "No está en tus favoritas",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (city.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón para abrir la Fuente de datos adicional mediante un Intent. Voy a usar Wikipedia
                    Button(
                        onClick = {
                            val formattedName = city.name.replace(" ", "_")
                            val url = "https://es.wikipedia.org/wiki/$formattedName"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Buscar información en Wikipedia")
                    }
                }
            }
        }
    }
}