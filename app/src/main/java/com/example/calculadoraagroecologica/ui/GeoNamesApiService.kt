package com.example.calculadoraagroecologica.ui

import retrofit2.http.GET
import retrofit2.http.Query

interface GeoNamesApiService {
    @GET("countryInfoJSON")
    suspend fun getCountryInfo(
        @Query("country") countryCode: String,
        @Query("username") username: String = "demo"
    ): GeoNamesResponse
}

data class GeoNamesResponse(
    val geonames: List<CountryInfo>
)

data class CountryInfo(
    val countryName: String,
    val north: Double,
    val south: Double,
    val east: Double,
    val west: Double,
    val areaInSqKm: Double
)
