package com.example.calculadoraagroecologica.ui // <--- ¡ESTA ES LA PRIMERA LÍNEA IMPORTANTE!

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraagroecologica.BuildConfig // Asegúrate que este esté
import com.example.calculadoraagroecologica.ui.model.Pais
// Quita los imports de EcoGreen, etc., si usas MaterialTheme.colorScheme

// Si OpenAIApiService, ChatRequest y Message están en este paquete 'ui',
// no necesitas los siguientes imports. Si están en otro, ajústalos.
import com.example.calculadoraagroecologica.ui.OpenAIApiService
import com.example.calculadoraagroecologica.ui.ChatRequest
import com.example.calculadoraagroecologica.ui.Message // Corregido "Mes" a "Message"
import kotlinx.coroutines.launch

// Función para obtener datos predefinidos de países
private fun obtenerDatosPredefinidos(pais: String): Pair<Float, Float>? {
    val datosPaises = mapOf(
        "mexico" to Pair(1973f, 1000f),
        "méxico" to Pair(1973f, 1000f),
        "peru" to Pair(1280f, 560f),
        "perú" to Pair(1280f, 560f),
        "colombia" to Pair(1142f, 440f),
        "argentina" to Pair(3694f, 1423f),
        "brasil" to Pair(4320f, 4320f),
        "chile" to Pair(4270f, 177f),
        "ecuador" to Pair(714f, 640f),
        "venezuela" to Pair(916f, 1011f),
        "bolivia" to Pair(1498f, 1290f),
        "paraguay" to Pair(1609f, 400f),
        "uruguay" to Pair(660f, 500f),
        "guyana" to Pair(800f, 436f),
        "surinam" to Pair(400f, 300f),
        "guayana francesa" to Pair(400f, 300f),
        "españa" to Pair(1000f, 800f),
        "spain" to Pair(1000f, 800f),
        "francia" to Pair(1000f, 1000f),
        "france" to Pair(1000f, 1000f),
        "alemania" to Pair(800f, 600f),
        "germany" to Pair(800f, 600f),
        "italia" to Pair(1200f, 300f),
        "italy" to Pair(1200f, 300f),
        "reino unido" to Pair(1000f, 500f),
        "united kingdom" to Pair(1000f, 500f),
        "uk" to Pair(1000f, 500f),
        "estados unidos" to Pair(4500f, 2700f),
        "united states" to Pair(4500f, 2700f),
        "usa" to Pair(4500f, 2700f),
        "canada" to Pair(5000f, 5000f),
        "china" to Pair(5000f, 4000f),
        "japon" to Pair(3000f, 300f),
        "japan" to Pair(3000f, 300f),
        "india" to Pair(3200f, 2900f),
        "australia" to Pair(4000f, 4000f),
        "rusia" to Pair(9000f, 4000f),
        "russia" to Pair(9000f, 4000f)
    )
    
    return datosPaises[pais.lowercase()]
}

@Composable
fun Modulo1Screen(viewModel: CalculadoraViewModel, onNext: () -> Unit) {
    var pais by remember { mutableStateOf("") }
    var largo by remember { mutableStateOf("") }
    var ancho by remember { mutableStateOf("") }
    var pd by remember { mutableStateOf<Float?>(null) }
    var showError by remember { mutableStateOf(false) }
    var showModulosMenu by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    var chatError by remember { mutableStateOf<String?>(null) }
    val colors = MaterialTheme.colorScheme
    val headerColor = if (colors.isLight()) colors.primary else colors.onBackground

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(
            text = "I-GDA - Indice de Dependencia Alimentaria",
            color = headerColor,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                android.util.Log.d("ChatGPT", "🚀 Botón ChatGPT presionado")
                loading = true
                chatError = null
                coroutineScope.launch {
                    try {
                        // Verificar que la API key esté configurada
                        if (BuildConfig.OPENAI_API_KEY.isBlank()) {
                            chatError = "🔑 Error: API Key no configurada. Agrega tu clave en local.properties"
                            return@launch
                        }
                        
                        // Log temporal para verificar que la API key se está leyendo
                        println("🔑 API Key configurada: ${BuildConfig.OPENAI_API_KEY.take(10)}...")
                        android.util.Log.d("ChatGPT", "🔑 API Key configurada: ${BuildConfig.OPENAI_API_KEY.take(10)}...")
                        
                        val api = OpenAIApiService.create(BuildConfig.OPENAI_API_KEY)
                        val prompt =
                            "¿Cuál es el largo y el ancho aproximado en kilómetros del país $pais? Responde solo los dos números, separados por coma. Ejemplo: 1200, 800"
                        val response = api.getChatCompletion(
                            ChatRequest(
                                messages = listOf(
                                    Message("user", prompt)
                                )
                            )
                        )
                        val answer = response.choices.firstOrNull()?.message?.content ?: ""
                        val regex = Regex("""([0-9]+(?:\.[0-9]+)?)""")
                        val numbers = regex.findAll(answer).map { it.value }.toList()
                        if (numbers.size >= 2) {
                            largo = numbers[0]
                            ancho = numbers[1]
                        } else {
                            // Si no se obtienen datos de la API, usar datos predefinidos
                            val datosPredefinidos = obtenerDatosPredefinidos(pais)
                            if (datosPredefinidos != null) {
                                largo = datosPredefinidos.first.toString()
                                ancho = datosPredefinidos.second.toString()
                                chatError = "⚠️ Usando datos predefinidos (API sin crédito)"
                            } else {
                                chatError = "No se pudo obtener la información. Intenta manualmente."
                            }
                        }
                    } catch (e: Exception) {
                        val errorMessage = when {
                            e.message?.contains("No address associated with hostname") == true -> 
                                "❌ Error de conexión: Verifica tu conexión a internet"
                            e.message?.contains("timeout") == true -> 
                                "⏰ Error de timeout: La conexión tardó demasiado"
                            e.message?.contains("401") == true -> 
                                "🔑 Error de API Key: Verifica tu clave de OpenAI"
                            e.message?.contains("429") == true || e.message?.contains("insufficient_quota") == true -> {
                                // Usar datos predefinidos cuando hay error de cuota
                                val datosPredefinidos = obtenerDatosPredefinidos(pais)
                                if (datosPredefinidos != null) {
                                    largo = datosPredefinidos.first.toString()
                                    ancho = datosPredefinidos.second.toString()
                                    "⚠️ Usando datos predefinidos (API sin crédito)"
                                } else {
                                    "🚫 Límite excedido: Intenta más tarde"
                                }
                            }
                            else -> "❌ Error: ${e.localizedMessage ?: "Error desconocido"}"
                        }
                        chatError = errorMessage
                    } finally {
                        loading = false
                    }
                }
            },
            enabled = !loading && pais.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50), // Verde claro más visible
                contentColor = Color.White, // Letras blancas
                disabledContainerColor = Color(0xFFBDBDBD), // Gris cuando está deshabilitado
                disabledContentColor = Color(0xFF757575) // Gris oscuro para texto deshabilitado
            ),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = if (loading) "Consultando..." else "🤖 Consultar datos con ChatGPT", 
                color = Color.White,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
        if (chatError != null) {
            Spacer(Modifier.height(8.dp))
            Text(
                chatError!!,
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { showModulosMenu = true },
            colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("📋 Ver módulos disponibles", color = colors.onSurface)
        }

        Box {
            DropdownMenu(
                expanded = showModulosMenu,
                onDismissRequest = { showModulosMenu = false },
                modifier = Modifier.background(colors.surface)
            ) {
                val modulos = listOf(
                    "🌍 Módulo 1: Ubicación del País",
                    "🍽️ Módulo 2: Ingreso de alimentos",
                    "📏 Módulo 3: Tablas de distancia",
                    "🚚 Módulo 4: Distancias de transporte",
                    "🛒 Módulo 5: Modos de adquisición",
                    "📊 Módulo 6: Cálculo de valores",
                    "📈 Módulo 7: Resultados finales"
                )

                modulos.forEach { modulo ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = modulo,
                                color = colors.onSurface,
                                fontSize = 12.sp
                            )
                        },
                        onClick = { showModulosMenu = false }
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = pais,
            onValueChange = { pais = it },
            label = { Text("Nombre del país", color = colors.onSurface) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = largo,
            onValueChange = { largo = it },
            label = { Text("Largo del país (km)") },
            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number), // Corregido para usar el import específico
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = ancho,
            onValueChange = { ancho = it },
            label = { Text("Ancho del país (km)") },
            keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number), // Corregido
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                val l = largo.toFloatOrNull()
                val a = ancho.toFloatOrNull()

                if (l != null && a != null && l > 0 && a > 0) {
                    pd = (l + a) / 2
                    showError = false
                } else {
                    showError = true
                    pd = null
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Calcular PD", color = colors.onPrimary)
        }

        if (showError) {
            Spacer(Modifier.height(8.dp))
            Text(
                "❌ Por favor ingresa valores válidos para largo y ancho",
                color = Color.Red,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        pd?.let {
            Spacer(Modifier.height(16.dp))
            Text(
                "✅ PD calculado: ${"%.2f".format(it)} km",
                color = colors.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val l = largo.toFloatOrNull() ?: 0f
                    val a = ancho.toFloatOrNull() ?: 0f
                    viewModel.updatePais(Pais(pais, l, a, pd!!))
                    onNext()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text("Continuar", color = colors.onPrimary)
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun Modulo1ScreenPreview() {
    // Para un Preview que funcione, necesitarás un ViewModel.
    // Si CalculadoraViewModel tiene dependencias (como Context),
    // no podrás instanciarlo directamente aquí.
    // En ese caso, puedes:
    // 1. Crear un ViewModel falso/mock para el Preview.
    // 2. O simplificar el Preview para no depender del ViewModel.
    // Por ahora, lo comento para evitar errores de compilación del Preview si el ViewModel es complejo.
    /*
    CalculadoraAgroecologicaTheme { // Asumiendo que tienes un tema así
        Modulo1Screen(
            viewModel = // viewModel de prueba o mock,
            onNext = {}
        )
    }
    */
    Text(
        text = "🌍 Calculadora Agroecológica\nMódulo 1: Ubicación del País (Preview)",
        color = Color.Black, // Usa un color que se vea en el fondo de Preview
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}