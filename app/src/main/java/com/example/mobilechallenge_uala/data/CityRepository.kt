package com.example.mobilechallenge_uala.data

import android.util.JsonReader
import com.example.mobilechallenge_uala.domain.City
import com.example.mobilechallenge_uala.domain.Coordinates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CityRepository {
    private val gistUrl = "https://gist.githubusercontent.com/hernan-uala/dce8843a8edbe0b0018b32e137bc2b3a/raw/0996accf70cb0ca0e16f9a99e0ee185fafca7af1/cities.json"

    suspend fun fetchCities(): List<City> = withContext(Dispatchers.IO) {
        val cities = mutableListOf<City>()
        var connection: HttpURLConnection? = null

        try {
            val url = URL(gistUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                // Usamos JsonReader para evitar outOfMemoryError por la ram
                val inputStream = connection.inputStream
                val reader = JsonReader(InputStreamReader(inputStream, "UTF-8"))

                reader.beginArray()
                while (reader.hasNext()) {
                    cities.add(readCity(reader))
                }
                reader.endArray()
                reader.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        } finally {
            connection?.disconnect()
        }

        return@withContext cities
    }

    // Leemos cada objeto JSON y lo mapeamos a nuestra Data Class City con sus atributos
    private fun readCity(reader: JsonReader): City {
        var country = ""
        var name = ""
        var id = 0
        var lat = 0.0
        var lon = 0.0

        reader.beginObject()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "country" -> country = reader.nextString()
                "name" -> name = reader.nextString()
                "_id" -> id = reader.nextInt()
                "coord" -> {
                    reader.beginObject()
                    while (reader.hasNext()) {
                        when (reader.nextName()) {
                            "lat" -> lat = reader.nextDouble()
                            "lon" -> lon = reader.nextDouble()
                            else -> reader.skipValue()
                        }
                    }
                    reader.endObject()
                }
                else -> reader.skipValue()
            }
        }
        reader.endObject()

        return City(
            id = id,
            name = name,
            country = country,
            coordinates = Coordinates(lat, lon),
            isFavorite = false
        )
    }
}