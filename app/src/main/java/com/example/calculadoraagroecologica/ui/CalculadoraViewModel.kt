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

private val Application.dataStore by preferencesDataStore(name = "calculadora_data")

class CalculadoraViewModel(app: Application) : AndroidViewModel(app) {
    private val _uiState = MutableStateFlow(CalculadoraData())
    val uiState: StateFlow<CalculadoraData> = _uiState

    private val DATA_KEY = stringPreferencesKey("calculadora_json")

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

    // Cálculo de WD_i para cada alimento
    fun calcularWD(alimento: Alimento): Float {
        val d = alimento.km
        val m = modoFactor[alimento.modo] ?: 1.5f
        val t = transporteFactor[alimento.transporte] ?: 1.3f
        return d * m * t
    }

    // Cálculo de I_GDA
    fun calcularIGDA(): Float {
        val alimentos = _uiState.value.alimentos
        val n = alimentos.size
        val pd = _uiState.value.pais.pd.takeIf { it > 0 } ?: 1f
        if (n == 0) return 0f
        val sumaWD = alimentos.sumOf { calcularWD(it).toDouble() }
        val igda = ((sumaWD / n) / pd).toFloat()
        return igda.coerceAtLeast(0f)
    }

    // Clasificación según I_GDA
    fun clasificacionIGDA(): Pair<Int, String> {
        val igda = calcularIGDA()
        return when {
            igda <= 0.25f -> 1 to "Local"
            igda <= 0.50f -> 2 to "Regional"
            igda <= 1.00f -> 3 to "Nacional"
            igda <= 2.00f -> 4 to "Continental"
            else -> 5 to "Internacional"
        }
    }

    init {
        viewModelScope.launch { loadData() }
    }

    fun saveData() {
        viewModelScope.launch {
            val json = Json.encodeToString(_uiState.value)
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[DATA_KEY] = json
            }
        }
    }

    suspend fun loadData() {
        val prefs = getApplication<Application>().dataStore.data.first()
        val json = prefs[DATA_KEY]
        if (json != null) {
            _uiState.value = Json.decodeFromString(json)
        }
    }

    fun updatePais(pais: Pais) {
        _uiState.value = _uiState.value.copy(pais = pais)
        saveData()
    }

    fun updateAlimentos(alimentos: List<Alimento>) {
        _uiState.value = _uiState.value.copy(alimentos = alimentos)
        saveData()
    }

    fun updateTablas(tablas: Tablas) {
        _uiState.value = _uiState.value.copy(tablas = tablas)
        saveData()
    }
} 