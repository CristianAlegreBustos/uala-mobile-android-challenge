# Ualá Mobile Challenge - Android Engineer

Este repositorio contiene la solución al desafío técnico para la posición de Mobile Engineer en Ualá. La aplicación está desarrollada 100% en Kotlin, utilizando Jetpack Compose para la UI y siguiendo estrictamente la regla de **no utilizar librerías de terceros** para la lógica de red, parseo JSON o inyección de dependencias.

## Enfoque del Algoritmo de Búsqueda (Search Problem)

El mayor desafío técnico era filtrar ~200.000 registros instantáneamente con cada pulsación de tecla ("as responsive as possible") sin trabar el hilo principal.

**Decisión:** En lugar de cargar los elementos en una lista plana (`List<City>`) y aplicar un `.filter{}` tradicional (lo cual tomaría tiempo $O(N)$ en cada pulsación), decidí implementar una estructura de datos de **Trie (Árbol de Prefijos)**.

**Por qué es más eficiente:**
1. **Búsqueda en $O(L)$:** El tiempo de búsqueda depende de la longitud del prefijo ($L$) y no de la cantidad total de ciudades ($N$).
2. **Preprocesamiento:** La lista original se ordena alfabéticamente (primero por ciudad, luego por país) *antes* de insertarse en el Trie. Esto garantiza que los resultados obtenidos de cualquier nodo ya vengan ordenados por defecto, evitando hacer un `.sorted()` en tiempo de ejecución durante la búsqueda.
3. **Manejo de Memoria:** Se preprocesa una sola vez al cargar la app y vive en memoria listo para responder a la UI a través del `StateFlow`.

## Decisiones de Arquitectura y Tecnologías

* **Parseo JSON "Vanilla":** Para evitar un `OutOfMemoryError` (OOM) al cargar un JSON de 200k registros de golpe, utilicé `android.util.JsonReader`. Esto permite leer el archivo como un flujo (stream) secuencial, lo cual es altamente eficiente en consumo de memoria RAM.
* **Patrón MVVM con UDF:** Utilicé `ViewModel` y `StateFlow` para mantener un flujo de datos unidireccional hacia Compose. La UI solo reacciona a los cambios de estado (`UiState`).
* **UI Dinámica (Jetpack Compose):** Se implementó una vista responsiva utilizando `LocalConfiguration.current.orientation`. En Portrait se utiliza navegación entre pantallas (Lista -> Mapa), mientras que en Landscape ambas vistas conviven en la misma pantalla usando `weight(1f)`.
* **Persistencia Nativa:** Para guardar los favoritos sin usar librerías externas (como Room), utilicé `SharedPreferences`, guardando un `Set<String>` con los IDs de las ciudades para lograr búsquedas y actualizaciones en tiempo $O(1)$.

## Nota importante sobre Google Maps SDK

La integración con el mapa interactivo está completamente implementada utilizando `com.google.maps.android:maps-compose`.

Actualmente, mi cuenta de Google Cloud Platform se encuentra a la espera de la verificación bancaria (el código de la transacción de habilitación de facturación) requerida por las nuevas políticas de Google para permitir la descarga de los *tiles* (imágenes) del mapa.

Por este motivo, la `API_KEY` provista en el `AndroidManifest.xml` renderiza el componente correctamente y centra la cámara en las coordenadas exactas con su respectivo marcador, pero el mapa de fondo puede aparecer en blanco.
* **Para probar la funcionalidad visual completa:** Se puede reemplazar el valor de la clave `com.google.android.geo.API_KEY` en el archivo `AndroidManifest.xml` por cualquier API Key propia que cuente con una cuenta de facturación activa.

## Testing
Se incluyen Unit Tests (`CitySearchEngineTest`) utilizando JUnit para demostrar la robustez del algoritmo de búsqueda del Trie, cubriendo búsquedas exitosas, case-insensitivity, orden alfabético y manejo de inputs inválidos.
