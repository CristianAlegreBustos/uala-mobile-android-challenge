package com.example.mobilechallenge_uala.domain

// 1. El nodo principal del  árbol
class TrieNode {
    // Usamos un mapa para conectar un carácter con el siguiente nodo
    val children = mutableMapOf<Char, TrieNode>()
    // Guardamos las ciudades que terminan EXACTAMENTE en este nodo
    val cities = mutableListOf<City>()
}

// 2. El motor de búsqueda
class CitySearchEngine {
    private val root = TrieNode()
    private var isInitialized = false

    // Preprocesamos la lista al iniciar la app
    fun insertCities(cities: List<City>) {
        if (isInitialized) return


        // Ordenamos la lista en orden alfabetico (Primero ciudad y luego pais) ANTES de insertarla para que las listas internas ya queden ordenadas.
        val sortedCities = cities.sortedWith(compareBy({ it.name.lowercase() }, { it.country.lowercase() }))

        for (city in sortedCities) {
            var current = root
            // Normalizamos el nombre de la ciudad para Búsqueda case insensitive. Guardamos la ciudad en minúsculas.
            val word = city.name.lowercase()

            for (char in word) {
                if (!current.children.containsKey(char)) {
                    current.children[char] = TrieNode()
                }
                current = current.children[char]!!
            }
            // guardamos la ciudad en ese nodo final
            current.cities.add(city)
        }
        isInitialized = true
    }

    // Filtrar por un string de prefijo.
    fun search(prefix: String): List<City> {
        if (prefix.isBlank()) return emptyList()

        var current = root
        // Normalizamos el input a minúscula
        val searchPrefix = prefix.lowercase()

        // 1. Navegamos por el árbol hasta el final del prefijo
        for (char in searchPrefix) {
            val node = current.children[char]
            if (node == null) {
                // Si una letra no está, el prefijo no existe. Devolvemos lista vacía.
                return emptyList()
            }
            current = node
        }

        // 2. Recolectamos todas las ciudades desde este nodo hacia abajo
        val results = mutableListOf<City>()
        collectAllCities(current, results)
        return results
    }

    // Función recursiva para juntar todas las ciudades que cuelgan del prefijo
    private fun collectAllCities(node: TrieNode, results: MutableList<City>) {
        // Agregamos las ciudades de este nodo ya ordenadas antes de agregar los hijos
        results.addAll(node.cities)

        // Para mantener el orden alfabético, recorremos los hijos ordenados por su llave (letra)
        val sortedKeys = node.children.keys.sorted()
        for (key in sortedKeys) {
            collectAllCities(node.children[key]!!, results)
        }
    }
}