package com.example.calculadoraagroecologica.ui

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

// Data classes para la respuesta de la API de países
data class CountryResponse(
    val name: CountryName,
    val area: Double,
    val borders: List<String>?,
    val region: String,
    val subregion: String?
)

data class CountryName(
    val common: String,
    val official: String
)

interface CountryApiService {
    @GET("v3.1/name/{countryName}")
    suspend fun getCountryByName(@Query("countryName") countryName: String): List<CountryResponse>
    
    @GET("v3.1/all")
    suspend fun getAllCountries(): List<CountryResponse>

    companion object {
        fun create(): CountryApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS) // Reducido para mejor UX móvil
                .readTimeout(15, TimeUnit.SECONDS)    // Reducido para mejor UX móvil
                .writeTimeout(15, TimeUnit.SECONDS)   // Reducido para mejor UX móvil
                .retryOnConnectionFailure(true)       // Reintentar en fallos de conexión
                .build()
                
            return Retrofit.Builder()
                .baseUrl("https://restcountries.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CountryApiService::class.java)
        }
    }
}


