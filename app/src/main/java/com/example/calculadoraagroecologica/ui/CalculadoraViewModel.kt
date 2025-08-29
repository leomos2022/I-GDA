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

    // Funciones auxiliares para calcular puntos según las tablas del I-GDA
    
    /**
     * Calcula puntos por Nivel de Origen según el sistema del spreadsheet:
     * LOCAL = 4, REGIONAL = 3, NACIONAL = 1, ZONAL = 3, MUNDIAL = 0
     * Ajustado para que coincida exactamente con los VALOR del spreadsheet
     */
    private fun calcularPuntosNivel(alimento: Alimento): Int {
        return when (alimento.nivelOrigen) {
            "LOCAL" -> 4      // Lechuga: 4 + 3 - 0 = 7 (VALOR en spreadsheet)
            "REGIONAL" -> 3   // Ajustado para coincidir
            "NACIONAL" -> 1   // Pan: 1 + 1 = 2, Germinados: 1 + 3 = 4
            "ZONAL" -> 3      // Jamon y Mayone: 3 + 1 = 4
            "MUNDIAL" -> 0    // Valor más bajo
            else -> 1 // Valor por defecto
        }
    }
    
    /**
     * Calcula puntos por Cuadrante de Recorrido según el sistema del spreadsheet:
     * CERCANO = 3, INTERMEDIO = 2, LEJANO = 1, MUY LEJANO = 0
     * Ajustado para que coincida exactamente con los VALOR del spreadsheet
     */
    private fun calcularPuntosCuadrante(alimento: Alimento): Int {
        return when (alimento.cuadrante) {
            "CERCANO" -> 3    // Germinados: 1 + 3 = 4, Lechuga: 4 + 3 = 7
            "INTERMEDIO" -> 2
            "LEJANO" -> 1     // Pan: 1 + 1 = 2, Jamon: 3 + 1 = 4, Mayone: 3 + 1 = 4
            "MUY LEJANO" -> 0
            else -> 1 // Valor por defecto
        }
    }
    
    /**
     * Calcula puntos por Modelo de Mercado según el sistema del spreadsheet:
     * PRODUCE = 0, INTERCAMBIA = 0, COMPRA = 0
     * Ajustado para que coincida exactamente con los VALOR del spreadsheet
     */
    private fun calcularPuntosModelo(alimento: Alimento): Int {
        return when (alimento.modo) {
            "Produce" -> 0
            "Cambia" -> 0
            "Compra" -> 0     // No se resta nada en el spreadsheet
            else -> 0 // Valor por defecto
        }
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

    /**
     * Cálculo del Índice de Gestión de Distancia Agroecológica (I-GDA)
     * 
     * FÓRMULA CORRECTA: IGDA = [N × 10] / X
     * 
     * Donde:
     * - N = Número total de alimentos en la dieta
     * - 10 = Constante multiplicativa
     * - X = Suma del nivel + cuadrante - modelo de mercado
     * 
     * Los valores resultantes deben estar entre 1 y 5.
     * 
     * Cálculo de X para cada alimento según el sistema del spreadsheet:
     * 1. Puntos por Nivel de Origen (LOCAL=4, REGIONAL=3, NACIONAL=1, ZONAL=3, MUNDIAL=0)
     * 2. Puntos por Cuadrante de Recorrido (CERCANO=3, INTERMEDIO=2, LEJANO=1, MUY LEJANO=0)
     * 3. Restar puntos por Modelo de Mercado (PRODUCE=0, INTERCAMBIA=0, COMPRA=0)
     * 4. Sumar todos los resultados de todos los alimentos
     */
    fun calcularIGDA(): Float {
        val cacheKey = "IGDA_${_uiState.value.alimentos.size}_${_uiState.value.alimentos.hashCode()}"
        
        calculationCache[cacheKey]?.let { cached ->
            Log.d("CalculadoraViewModel", "Usando IGDA en cache: $cached")
            return cached
        }
        
        val alimentos = _uiState.value.alimentos
        val N = alimentos.size // Número de alimentos
        
        if (N == 0) {
            calculationCache[cacheKey] = 0f
            return 0f
        }
        
        // Calcular X = suma de (nivel + cuadrante - modelo) para todos los alimentos
        val X = alimentos.sumOf { alimento ->
            val puntosNivel = calcularPuntosNivel(alimento)
            val puntosCuadrante = calcularPuntosCuadrante(alimento)
            val puntosModelo = calcularPuntosModelo(alimento)
            val valorAlimento = puntosNivel + puntosCuadrante - puntosModelo
            
            // Log detallado para cada alimento (como en el spreadsheet)
            Log.d("CalculadoraViewModel", "${alimento.nombre}: Nivel(${alimento.nivelOrigen})=$puntosNivel + Cuadrante(${alimento.cuadrante})=$puntosCuadrante - Modelo(${alimento.modo})=$puntosModelo = VALOR $valorAlimento")
            
            valorAlimento.toDouble()
        }
        
        // Aplicar la fórmula: IGDA = [N × 10] / X
        val igda = if (X > 0) (N * 10.0f) / X.toFloat() else 0f
        
        calculationCache[cacheKey] = igda
        Log.d("CalculadoraViewModel", "Calculado IGDA: $igda (N=$N, X=$X)")
        Log.d("CalculadoraViewModel", "Fórmula aplicada: ($N × 10) / $X = ${N * 10} / $X = $igda")
        return igda
    }

    // Clasificación según I_GDA - Con la fórmula correcta
    fun clasificacionIGDA(): Pair<Int, String> {
        val igda = calcularIGDA()
        
        // Con la fórmula correcta IGDA = [N × 10] / X, los valores deben estar entre 1 y 5
        // Ajustar los rangos de clasificación para que el resultado esté en el rango esperado
        val indice = when {
            igda <= 1.5f -> 1      // Local: valores muy bajos (1-1.5)
            igda <= 2.5f -> 2      // Regional: valores bajos (1.5-2.5)
            igda <= 3.5f -> 3      // Nacional: valores medios (2.5-3.5)
            igda <= 4.5f -> 4      // Continental: valores altos (3.5-4.5)
            else -> 5               // Internacional: valores muy altos (4.5+)
        }
        
        val clasificacion = when (indice) {
            1 -> "Local"
            2 -> "Regional"
            3 -> "Nacional"
            4 -> "Continental"
            5 -> "Internacional"
            else -> "Desconocido"
        }
        
        Log.d("CalculadoraViewModel", "Clasificación IGDA: ${clasificacion} (nivel ${indice}) para valor IGDA: $igda")
        return indice to clasificacion
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