package com.example.mobilechallenge_uala.domain

data class City(
    val id: Int,
    val name: String,
    val country: String,
    val coordinates: Coordinates,
    var isFavorite: Boolean = false
)

data class Coordinates(
    val lat: Double,
    val lon: Double
)
