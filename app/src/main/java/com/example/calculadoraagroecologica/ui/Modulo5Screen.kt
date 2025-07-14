package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraagroecologica.ui.theme.EcoGreen
import com.example.calculadoraagroecologica.ui.theme.WhiteText
import com.example.calculadoraagroecologica.ui.theme.CharcoalGray
import com.example.calculadoraagroecologica.ui.theme.StoneGray
import com.example.calculadoraagroecologica.ui.CalculadoraViewModel
import com.example.calculadoraagroecologica.ui.model.Alimento
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Modulo5Screen(
    viewModel: CalculadoraViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPrint: () -> Unit,
    onSendEmail: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val alimentos = remember { mutableStateListOf<Alimento>().apply { addAll(uiState.alimentos) } }
    val opciones = listOf("Compra", "Cambia", "Produce")
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
        // Botón de ayuda/información arriba del título
        Button(
            onClick = { /* Aquí puedes agregar funcionalidad de ayuda */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3), // Azul para botón de ayuda
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(bottom = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("ℹ️", fontSize = 16.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Información sobre los modos",
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                )
            }
        }
        Text(
            text = "Módulo 5",
            color = colors.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Modo de adquisición y transporte",
            color = colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "¿Cómo se consiguió cada alimento?",
            color = colors.onSurface,
            style = MaterialTheme.typography.bodyLarge, // Más como subtítulo o explicación
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )


        Spacer(Modifier.height(32.dp))
        
        // Indicador de progreso
        val alimentosCompletados = alimentos.count { it.modo.isNotBlank() }
        val totalAlimentos = alimentos.size
        
        Text(
            text = "Progreso: $alimentosCompletados de $totalAlimentos alimentos configurados",
            color = colors.onSurfaceVariant,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Barra de progreso visual
        androidx.compose.material3.LinearProgressIndicator(
            progress = if (totalAlimentos > 0) alimentosCompletados.toFloat() / totalAlimentos else 0f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = colors.primary,
            trackColor = colors.surfaceVariant
        )
        
        Spacer(Modifier.height(24.dp))
        
        alimentos.forEachIndexed { i, alimento ->
            var expanded by remember { mutableStateOf(false) }
            var transporteExpanded by remember { mutableStateOf(false) }
            val transporteOpciones = listOf("Pie/Bici", "Camión", "Barco", "Avión")
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                // Título del alimento
                Text(
                    text = alimento.nombre,
                    color = colors.primary,
                    fontSize = 16.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(8.dp))
                
                // Dropdown para seleccionar el modo
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Botón principal que abre el dropdown
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (alimento.modo.isBlank()) 
                                Color(0xFFE3F2FD) // Azul claro cuando no hay selección
                            else 
                                Color(0xFF4CAF50), // Verde cuando hay selección
                            contentColor = if (alimento.modo.isBlank()) 
                                Color(0xFF1976D2) // Azul oscuro para texto
                            else 
                                Color.White // Blanco para texto
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (alimento.modo.isBlank()) "Selecciona el modo" else alimento.modo,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                            Icon(
                                imageVector = if (expanded) 
                                    androidx.compose.material.icons.Icons.Default.KeyboardArrowUp 
                                else 
                                    androidx.compose.material.icons.Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expandir menú",
                                tint = if (alimento.modo.isBlank()) 
                                    Color(0xFF1976D2) 
                                else 
                                    Color.White
                            )
                        }
                    }
                    // Menú desplegable
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .background(colors.surface)
                    ) {
                        opciones.forEach { option ->
                            DropdownMenuItem(
                                text = { 
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = when (option) {
                                                "Compra" -> "🛒 Compra"
                                                "Cambia" -> "🔄 Cambia"
                                                "Produce" -> "🌱 Produce"
                                                else -> option
                                            },
                                            color = colors.onSurface,
                                            fontSize = 16.sp,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                        )
                                    }
                                },
                                onClick = {
                                    alimentos[i] = alimento.copy(modo = option)
                                    expanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                // Dropdown para seleccionar el transporte
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { transporteExpanded = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (alimento.transporte.isBlank()) 
                                Color(0xFFFFF3E0) // Naranja claro cuando no hay selección
                            else 
                                Color(0xFFFB8C00), // Naranja cuando hay selección
                            contentColor = if (alimento.transporte.isBlank()) 
                                Color(0xFFFB8C00) // Naranja para texto
                            else 
                                Color.White // Blanco para texto
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (alimento.transporte.isBlank()) "Selecciona el transporte" else alimento.transporte,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                            Icon(
                                imageVector = if (transporteExpanded) 
                                    androidx.compose.material.icons.Icons.Default.KeyboardArrowUp 
                                else 
                                    androidx.compose.material.icons.Icons.Default.KeyboardArrowDown,
                                contentDescription = "Expandir menú transporte",
                                tint = if (alimento.transporte.isBlank()) 
                                    Color(0xFFFB8C00) 
                                else 
                                    Color.White
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = transporteExpanded,
                        onDismissRequest = { transporteExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .background(colors.surface)
                    ) {
                        transporteOpciones.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option, color = colors.onSurface, fontSize = 16.sp) },
                                onClick = {
                                    alimentos[i] = alimento.copy(transporte = option)
                                    transporteExpanded = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                
                // Indicador visual del modo seleccionado
                if (alimento.modo.isNotBlank()) {
                    Spacer(Modifier.height(8.dp))
                    androidx.compose.material3.Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material3.CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E8) // Verde muy claro
                        ),
                        elevation = androidx.compose.material3.CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (alimento.modo) {
                                    "Compra" -> "🛒"
                                    "Cambia" -> "🔄"
                                    "Produce" -> "🌱"
                                    else -> "✅"
                                },
                                fontSize = 20.sp
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = when (alimento.modo) {
                                    "Compra" -> "Se compra en el mercado"
                                    "Cambia" -> "Se obtiene por intercambio"
                                    "Produce" -> "Se produce localmente"
                                    else -> "Modo seleccionado"
                                },
                                color = Color(0xFF2E7D32), // Verde oscuro
                                fontSize = 14.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        }
                    }
                } else {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "💡 Haz clic en el botón para seleccionar el modo",
                        color = colors.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        Spacer(Modifier.height(32.dp))
        
        // Botones apilados verticalmente
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = colors.surface),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Regresar", color = colors.onSurface)
        }
        
        Spacer(Modifier.height(12.dp))
        
        Button(
            onClick = onPrint,
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("🖨️ Imprimir", color = colors.onPrimary)
        }
        
        Spacer(Modifier.height(12.dp))
        
        Button(
            onClick = onSendEmail,
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("📧 Enviar correo", color = colors.onPrimary)
        }
        
        Spacer(Modifier.height(12.dp))
        
        Button(
            onClick = {
                viewModel.updateAlimentos(alimentos.toList())
                onNext()
            },
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            enabled = alimentos.all { it.modo.isNotBlank() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = if (alimentos.all { it.modo.isNotBlank() }) {
                    "✅ Continuar al siguiente módulo"
                } else {
                    "⏳ Completa todos los alimentos"
                },
                color = colors.onPrimary
            )
        }
        
        Spacer(Modifier.height(32.dp))
    }
} 