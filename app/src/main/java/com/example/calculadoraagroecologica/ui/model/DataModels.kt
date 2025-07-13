package com.example.calculadoraagroecologica.ui.model

import kotlinx.serialization.Serializable

@Serializable
data class Pais(
    val nombre: String = "",
    val largo: Float = 0f,
    val ancho: Float = 0f,
    val pd: Float = 0f
)

@Serializable
data class Alimento(
    val nombre: String = "",
    val km: Float = 0f,
    val nivel: String = "",
    val categoria: String = "",
    val modo: String = "Compra",
    val valorAcumulado: Int = 0
)

@Serializable
data class Tablas(
    val mundial: Map<String, Float> = emptyMap(),
    val continental: Map<String, Float> = emptyMap(),
    val nacional: Map<String, Float> = emptyMap(),
    val regional: Map<String, Float> = emptyMap(),
    val zonal: Map<String, Float> = emptyMap(),
    val local: Map<String, Float> = emptyMap()
)

@Serializable
data class CalculadoraData(
    val pais: Pais = Pais(),
    val alimentos: List<Alimento> = emptyList(),
    val tablas: Tablas = Tablas()
) 