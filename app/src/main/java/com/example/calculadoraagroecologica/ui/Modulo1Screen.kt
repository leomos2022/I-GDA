package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import com.example.calculadoraagroecologica.ui.model.Pais

@Composable
fun Modulo1Screen(
    viewModel: CalculadoraViewModel,
    onNext: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    
    var countryCode by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var countryInfo by remember { mutableStateOf<CountryCalculationResult?>(null) }
    var errorMessage by remember { mutableStateOf("") }
    
    val countryRepository = remember { CountryRepository() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = colors.primaryContainer),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🌍 Módulo 1",
                    color = colors.onPrimaryContainer,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Índice de Dependencia Alimentaria",
                    color = colors.onPrimaryContainer,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingresa el código de país para calcular las dimensiones geográficas",
                    color = colors.onPrimaryContainer.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Formulario de entrada
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Código de País",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Ingresa el código ISO del país (ej: CO, MX, AR, BR, ES, US)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = countryCode,
                    onValueChange = { countryCode = it.uppercase() },
                    label = { Text("Código (ej: CO)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colors.primary,
                        unfocusedBorderColor = colors.outline
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        if (countryCode.isNotBlank()) {
                            isLoading = true
                            errorMessage = ""
                            
                            scope.launch {
                                try {
                                    val result = countryRepository.getCountryInfo(countryCode)
                                    when (result) {
                                        is CountryApiResult.Success -> {
                                            countryInfo = result.data
                                            Toast.makeText(context, "País encontrado: ${result.data.nombre}", Toast.LENGTH_SHORT).show()
                                        }
                                        is CountryApiResult.Fallback -> {
                                            countryInfo = result.data
                                            Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                        }
                                        is CountryApiResult.Error -> {
                                            errorMessage = result.message
                                            Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "Error: ${e.message}"
                                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                                } finally {
                                    isLoading = false
                                }
                            }
                        } else {
                            Toast.makeText(context, "Por favor ingresa un código de país", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading && countryCode.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = colors.primary)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = colors.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("🔍 Buscar País", color = colors.onPrimary)
                    }
                }
            }
        }
        
        // Mostrar información del país si está disponible
        countryInfo?.let { info ->
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = colors.secondaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "📊 Información del País",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSecondaryContainer
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoItem("Nombre", info.nombre, "🏛️", colors)
                        InfoItem("Largo", "${info.largo.toInt()} km", "📏", colors)
                        InfoItem("Ancho", "${info.ancho.toInt()} km", "📐", colors)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = colors.primaryContainer.copy(alpha = 0.3f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🌍 Dimensión Promedio (PD)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.onPrimaryContainer
                            )
                            Text(
                                text = "${info.pd.toInt()} km",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
        
        // Mostrar mensaje de error si existe
        if (errorMessage.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "❌ $errorMessage",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Botón de siguiente
        Button(
            onClick = {
                if (countryInfo != null) {
                    viewModel.updatePais(
                        Pais(
                            nombre = countryInfo!!.nombre,
                            largo = countryInfo!!.largo,
                            ancho = countryInfo!!.ancho,
                            pd = countryInfo!!.pd
                        )
                    )
                    onNext()
                } else {
                    Toast.makeText(context, "Primero debes buscar un país", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            enabled = countryInfo != null,
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Siguiente →", color = colors.onPrimary)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InfoItem(label: String, value: String, icon: String, colors: ColorScheme) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 24.sp)
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colors.onSecondaryContainer
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = colors.onSecondaryContainer.copy(alpha = 0.8f)
        )
    }
}
