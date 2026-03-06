package com.example.mobilechallenge_uala.domain

import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CitySearchEngineTest {

    private lateinit var searchEngine: CitySearchEngine

    @Before
    fun setup() {
        searchEngine = CitySearchEngine()

        // Armamos una lista de prueba basada en los ejemplos del Json
        val mockCities = listOf(
            City(1, "Alabama", "US", Coordinates(0.0, 0.0)),
            City(2, "Albuquerque", "US", Coordinates(0.0, 0.0)),
            City(3, "Anaheim", "US", Coordinates(0.0, 0.0)),
            City(4, "Arizona", "US", Coordinates(0.0, 0.0)),
            City(5, "Sydney", "AU", Coordinates(0.0, 0.0))
        )

        searchEngine.insertCities(mockCities)
    }

    @Test
    fun `Si el prefijo es A, deben aparecer todas las ciudades excepto Sydney`() {
        val results = searchEngine.search("A")

        Assert.assertEquals(4, results.size)
        Assert.assertTrue(results.none { it.name == "Sydney" })
    }

    @Test
    fun `Si el prefijo es s (minuscula), el unico resultado debe ser Sydney`() {
        //Test case insensitive
        val results = searchEngine.search("s")

        Assert.assertEquals(1, results.size)
        Assert.assertEquals("Sydney", results[0].name)
    }

    @Test
    fun `Si el prefijo es Al, solo deben aparecer Alabama y Albuquerque`() {
        val results = searchEngine.search("Al")

        Assert.assertEquals(2, results.size)
        Assert.assertTrue(results.any { it.name == "Alabama" })
        Assert.assertTrue(results.any { it.name == "Albuquerque" })
    }

    @Test
    fun `Si el prefijo es Alb, el unico resultado es Albuquerque`() {
        val results = searchEngine.search("Alb")

        Assert.assertEquals(1, results.size)
        Assert.assertEquals("Albuquerque", results[0].name)
    }

    @Test
    fun `Si el input es invalido o no existe, debe retornar una lista vacia`() {
        val results = searchEngine.search("Zebra")

        Assert.assertTrue(results.isEmpty())
    }
}