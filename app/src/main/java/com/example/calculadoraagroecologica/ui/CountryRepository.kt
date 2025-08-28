package com.example.calculadoraagroecologica.ui

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.cos
import kotlin.math.PI
import android.util.Log

class CountryRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.geonames.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(GeoNamesApiService::class.java)
    
    // Cache local para evitar consultas repetidas
    private val cache = mutableMapOf<String, CountryCalculationResult>()

    suspend fun getCountryInfo(countryCode: String): CountryApiResult {
        return withContext(Dispatchers.IO) {
            try {
                // Verificar cache primero
                cache[countryCode]?.let { cached ->
                    Log.d("CountryRepository", "Usando datos en cache para $countryCode")
                    return@withContext CountryApiResult.Success(cached)
                }
                
                // Intentar con la API
                val response = apiService.getCountryInfo(countryCode, "demo")
                if (response.geonames.isNotEmpty()) {
                    val countryInfo = response.geonames[0]
                    val result = calculateCountryDimensions(countryInfo)
                    
                    // Guardar en cache
                    cache[countryCode] = result
                    
                    Log.d("CountryRepository", "Datos obtenidos de API para $countryCode")
                    CountryApiResult.Success(result)
                } else {
                    // Si no hay resultados, usar datos predefinidos
                    val datosPredefinidos = getDatosPredefinidos(countryCode)
                    if (datosPredefinidos != null) {
                        cache[countryCode] = datosPredefinidos
                        CountryApiResult.Fallback(datosPredefinidos, "No se encontraron datos en la API, usando datos predefinidos")
                    } else {
                        CountryApiResult.Error("Código de país no reconocido: $countryCode")
                    }
                }
            } catch (e: Exception) {
                Log.e("CountryRepository", "Error al obtener datos para $countryCode: ${e.message}")
                
                // En caso de error de red, usar datos predefinidos
                val datosPredefinidos = getDatosPredefinidos(countryCode)
                if (datosPredefinidos != null) {
                    cache[countryCode] = datosPredefinidos
                    CountryApiResult.Fallback(datosPredefinidos, "Error de conexión, usando datos predefinidos: ${e.message}")
                } else {
                    CountryApiResult.Error("Código de país no reconocido: $countryCode")
                }
            }
        }
    }

    private fun getDatosPredefinidos(countryCode: String): CountryCalculationResult? {
        val datosPaises = mapOf(
            "CO" to CountryCalculationResult("Colombia", 1142f, 440f, 791f),
            "MX" to CountryCalculationResult("México", 1973f, 1000f, 1486.5f),
            "AR" to CountryCalculationResult("Argentina", 3694f, 1423f, 2558.5f),
            "BR" to CountryCalculationResult("Brasil", 4320f, 4320f, 4320f),
            "CL" to CountryCalculationResult("Chile", 4270f, 177f, 2223.5f),
            "PE" to CountryCalculationResult("Perú", 1280f, 560f, 920f),
            "EC" to CountryCalculationResult("Ecuador", 714f, 640f, 677f),
            "VE" to CountryCalculationResult("Venezuela", 916f, 1011f, 963.5f),
            "BO" to CountryCalculationResult("Bolivia", 1498f, 1290f, 1394f),
            "PY" to CountryCalculationResult("Paraguay", 1609f, 400f, 1004.5f),
            "UY" to CountryCalculationResult("Uruguay", 660f, 500f, 580f),
            "ES" to CountryCalculationResult("España", 1000f, 800f, 900f),
            "FR" to CountryCalculationResult("Francia", 1000f, 1000f, 1000f),
            "DE" to CountryCalculationResult("Alemania", 800f, 600f, 700f),
            "IT" to CountryCalculationResult("Italia", 1200f, 300f, 750f),
            "UK" to CountryCalculationResult("Reino Unido", 1000f, 500f, 750f),
            "US" to CountryCalculationResult("Estados Unidos", 4500f, 2700f, 3600f),
            "CA" to CountryCalculationResult("Canadá", 5000f, 5000f, 5000f),
            "CN" to CountryCalculationResult("China", 5000f, 4000f, 4500f),
            "JP" to CountryCalculationResult("Japón", 3000f, 300f, 1650f),
            "IN" to CountryCalculationResult("India", 3200f, 2900f, 3050f),
            "AU" to CountryCalculationResult("Australia", 4000f, 4000f, 4000f),
            "RU" to CountryCalculationResult("Rusia", 9000f, 4000f, 6500f)
        )
        
        return datosPaises[countryCode.uppercase()]
    }

    private fun calculateCountryDimensions(countryInfo: CountryInfo): CountryCalculationResult {
        // Convertir grados a km
        // 1° latitud ≈ 111 km
        // 1° longitud ≈ 111 km * cos(latitud promedio)
        val latPromedio = (countryInfo.north + countryInfo.south) / 2
        val latPromedioRad = latPromedio * PI / 180
        
        val largo = (countryInfo.north - countryInfo.south) * 111.0
        val ancho = (countryInfo.east - countryInfo.west) * 111.0 * cos(latPromedioRad)
        
        val pd = (largo + ancho) / 2
        
        return CountryCalculationResult(
            nombre = countryInfo.countryName,
            largo = largo.toFloat(),
            ancho = ancho.toFloat(),
            pd = pd.toFloat()
        )
    }
    
    // Limpiar cache cuando sea necesario
    fun clearCache() {
        cache.clear()
        Log.d("CountryRepository", "Cache limpiado")
    }
}

// Modelos de datos para este repositorio
data class CountryCalculationResult(
    val nombre: String,
    val largo: Float,
    val ancho: Float,
    val pd: Float
)

sealed class CountryApiResult {
    data class Success(val data: CountryCalculationResult) : CountryApiResult()
    data class Fallback(val data: CountryCalculationResult, val message: String) : CountryApiResult()
    data class Error(val message: String) : CountryApiResult()
}
