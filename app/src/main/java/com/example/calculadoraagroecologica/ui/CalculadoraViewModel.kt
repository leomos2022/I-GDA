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