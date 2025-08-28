package com.example.calculadoraagroecologica.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import com.example.calculadoraagroecologica.ui.model.CalculadoraData
import com.example.calculadoraagroecologica.ui.model.Pais
import com.example.calculadoraagroecologica.ui.model.Alimento
import com.example.calculadoraagroecologica.ui.model.Tablas
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.util.Log

private val Application.dataStore by preferencesDataStore(name = "calculadora_data")

class CalculadoraViewModel(app: Application) : AndroidViewModel(app) {
    private val _uiState = MutableStateFlow(CalculadoraData())
    val uiState: StateFlow<CalculadoraData> = _uiState

    private val DATA_KEY = stringPreferencesKey("calculadora_json")
    
    // Cache local para cálculos
    private val calculationCache = mutableMapOf<String, Float>()

    // Factores para modo de adquisición
    private val modoFactor = mapOf(
        "Produce" to 1.0f,
        "Cambia" to 1.2f,
        "Compra" to 1.5f
    )
    
    // Factores para medio de transporte
    private val transporteFactor = mapOf(
        "Pie/Bici" to 1.0f,
        "Camión" to 1.3f,
        "Barco" to 1.8f,
        "Avión" to 3.0f
    )

    // Cálculo de WD_i para cada alimento con cache
    fun calcularWD(alimento: Alimento): Float {
        val cacheKey = "WD_${alimento.nombre}_${alimento.km}_${alimento.modo}_${alimento.transporte}"
        
        calculationCache[cacheKey]?.let { cached ->
            Log.d("CalculadoraViewModel", "Usando WD en cache para ${alimento.nombre}")
            return cached
        }
        
        val d = alimento.km
        val m = modoFactor[alimento.modo] ?: 1.5f
        val t = transporteFactor[alimento.transporte] ?: 1.3f
        val result = d * m * t
        
        calculationCache[cacheKey] = result
        Log.d("CalculadoraViewModel", "Calculado WD para ${alimento.nombre}: $result")
        
        return result
    }

    // Cálculo de huella de carbono con cache
    fun calcularHuellaCarbono(alimento: Alimento): Float {
        val cacheKey = "CO2_${alimento.nombre}_${alimento.km}_${alimento.transporte}"
        
        calculationCache[cacheKey]?.let { cached ->
            Log.d("CalculadoraViewModel", "Usando CO2 en cache para ${alimento.nombre}")
            return cached
        }
        
        val factorTransporte = when (alimento.transporte) {
            "Pie/Bici" -> 1.0f
            "Camión" -> 1.3f
            "Barco" -> 1.8f
            "Avión" -> 3.0f
            else -> 1.3f
        }
        
        val result = alimento.km * factorTransporte
        calculationCache[cacheKey] = result
        
        Log.d("CalculadoraViewModel", "Calculado CO2 para ${alimento.nombre}: $result")
        return result
    }

    // Evaluación de sostenibilidad mejorada
    fun evaluarSostenibilidad(huellaCarbono: Float): String {
        return when {
            huellaCarbono < 10f -> "Alta"
            huellaCarbono <= 30f -> "Media"
            else -> "Baja"
        }
    }

    // Actualizar alimentos con cálculos de sostenibilidad
    fun actualizarAlimentosConSostenibilidad(alimentos: List<Alimento>): List<Alimento> {
        Log.d("CalculadoraViewModel", "Actualizando ${alimentos.size} alimentos con sostenibilidad")
        
        return alimentos.map { alimento ->
            val huellaCarbono = calcularHuellaCarbono(alimento)
            val sostenibilidad = evaluarSostenibilidad(huellaCarbono)
            
            alimento.copy(
                huellaCarbono = huellaCarbono,
                sostenibilidad = sostenibilidad
            )
        }
    }

    // Cálculo de I_GDA con cache
    fun calcularIGDA(): Float {
        val cacheKey = "IGDA_${_uiState.value.alimentos.size}_${_uiState.value.pais.pd}"
        
        calculationCache[cacheKey]?.let { cached ->
            Log.d("CalculadoraViewModel", "Usando IGDA en cache: $cached")
            return cached
        }
        
        val alimentos = _uiState.value.alimentos
        val n = alimentos.size
        val pd = _uiState.value.pais.pd.takeIf { it > 0 } ?: 1f
        
        if (n == 0) {
            calculationCache[cacheKey] = 0f
            return 0f
        }
        
        val sumaWD = alimentos.sumOf { calcularWD(it).toDouble() }
        val igda = ((sumaWD / n) / pd).toFloat()
        val result = igda.coerceAtLeast(0f)
        
        calculationCache[cacheKey] = result
        Log.d("CalculadoraViewModel", "Calculado IGDA: $result")
        
        return result
    }

    // Clasificación según I_GDA
    fun clasificacionIGDA(): Pair<Int, String> {
        val igda = calcularIGDA()
        val clasificacion = when {
            igda <= 0.25f -> 1 to "Local"
            igda <= 0.50f -> 2 to "Regional"
            igda <= 1.00f -> 3 to "Nacional"
            igda <= 2.00f -> 4 to "Continental"
            else -> 5 to "Internacional"
        }
        
        Log.d("CalculadoraViewModel", "Clasificación IGDA: ${clasificacion.second} (nivel ${clasificacion.first})")
        return clasificacion
    }
    
    // Limpiar cache cuando sea necesario
    fun clearCache() {
        calculationCache.clear()
        Log.d("CalculadoraViewModel", "Cache de cálculos limpiado")
    }
    
    // Obtener estadísticas de los alimentos
    fun obtenerEstadisticasAlimentos(): Map<String, Any> {
        val alimentos = _uiState.value.alimentos
        if (alimentos.isEmpty()) return emptyMap()
        
        val totalAlimentos = alimentos.size
        val promedioValor = alimentos.map { it.valorAcumulado }.average()
        val promedioCO2 = alimentos.map { it.huellaCarbono }.average()
        val sostenibilidadAlta = alimentos.count { it.sostenibilidad == "Alta" }
        val sostenibilidadMedia = alimentos.count { it.sostenibilidad == "Media" }
        val sostenibilidadBaja = alimentos.count { it.sostenibilidad == "Baja" }
        
        return mapOf(
            "totalAlimentos" to totalAlimentos,
            "promedioValor" to promedioValor,
            "promedioCO2" to promedioCO2,
            "sostenibilidadAlta" to sostenibilidadAlta,
            "sostenibilidadMedia" to sostenibilidadMedia,
            "sostenibilidadBaja" to sostenibilidadBaja
        )
    }

    init {
        viewModelScope.launch { loadData() }
    }

    fun saveData() {
        viewModelScope.launch {
            try {
                val json = Json.encodeToString(_uiState.value)
                getApplication<Application>().dataStore.edit { prefs ->
                    prefs[DATA_KEY] = json
                }
                Log.d("CalculadoraViewModel", "Datos guardados exitosamente")
            } catch (e: Exception) {
                Log.e("CalculadoraViewModel", "Error al guardar datos: ${e.message}")
            }
        }
    }

    suspend fun loadData() {
        try {
            val prefs = getApplication<Application>().dataStore.data.first()
            val json = prefs[DATA_KEY]
            if (json != null) {
                _uiState.value = Json.decodeFromString(json)
                Log.d("CalculadoraViewModel", "Datos cargados exitosamente")
            } else {
                Log.d("CalculadoraViewModel", "No hay datos previos para cargar")
            }
        } catch (e: Exception) {
            Log.e("CalculadoraViewModel", "Error al cargar datos: ${e.message}")
        }
    }

    fun updatePais(pais: Pais) {
        _uiState.value = _uiState.value.copy(pais = pais)
        saveData()
        clearCache() // Limpiar cache cuando cambie el país
        Log.d("CalculadoraViewModel", "País actualizado: ${pais.nombre}")
    }

    fun updateAlimentos(alimentos: List<Alimento>) {
        _uiState.value = _uiState.value.copy(alimentos = alimentos)
        saveData()
        clearCache() // Limpiar cache cuando cambien los alimentos
        Log.d("CalculadoraViewModel", "Alimentos actualizados: ${alimentos.size} elementos")
    }

    fun updateTablas(tablas: Tablas) {
        _uiState.value = _uiState.value.copy(tablas = tablas)
        saveData()
        Log.d("CalculadoraViewModel", "Tablas actualizadas")
    }
} 