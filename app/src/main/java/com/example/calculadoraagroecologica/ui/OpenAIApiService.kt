package com.example.calculadoraagroecologica.ui

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

// Data classes para la petición y respuesta

data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Message>
)
data class Message(
    val role: String,
    val content: String
)
data class ChatResponse(
    val choices: List<Choice>
)
data class Choice(
    val message: Message
)

// Interceptor para la API Key
class AuthInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $apiKey")
            .build()
        return chain.proceed(request)
    }
}

interface OpenAIApiService {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(@Body request: ChatRequest): ChatResponse

    companion object {
        fun create(apiKey: String): OpenAIApiService {
            // Configurar logging para debug
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            
            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor(apiKey))
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
                
            return Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenAIApiService::class.java)
        }
    }
} 