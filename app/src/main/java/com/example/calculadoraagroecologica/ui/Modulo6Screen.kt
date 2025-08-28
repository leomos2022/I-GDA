package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import com.example.calculadoraagroecologica.ui.CalculadoraViewModel
import com.example.calculadoraagroecologica.ui.model.Alimento

@Composable
fun Modulo6Screen(
    viewModel: CalculadoraViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPrint: () -> Unit,
    onSendEmail: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val alimentos = remember { mutableStateListOf<Alimento>().apply { addAll(uiState.alimentos) } }
    val nivelMap = mapOf("Mundial" to 1, "Continental" to 2, "Nacional" to 3, "Regional" to 4, "Zonal" to 5, "Local" to 6)
    val catMap = mapOf("Muy lejano" to 1, "Lejano" to 2, "Intermedio" to 3, "Cercano" to 4)
    val modoMap = mapOf("Compra" to 3, "Cambia" to 1, "Produce" to 0)
    
    // Detectar orientación y tamaño de pantalla
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
    val isTablet = configuration.screenWidthDp >= 600

    // Calcular resultados con sostenibilidad
    val resultados = remember {
        alimentos.map { alimento ->
            val n = nivelMap[alimento.nivel] ?: 0
            val c = catMap[alimento.categoria] ?: 0
            val m = modoMap[alimento.modo] ?: 0
            val valor = (n + c) - m
            
            // Calcular huella de carbono y sostenibilidad
            val huellaCarbono = viewModel.calcularHuellaCarbono(alimento)
            val sostenibilidad = viewModel.evaluarSostenibilidad(huellaCarbono)
            
            alimento.copy(
                valorAcumulado = valor,
                huellaCarbono = huellaCarbono,
                sostenibilidad = sostenibilidad
            )
        }
    }

    val colors = MaterialTheme.colorScheme
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header mejorado
        Spacer(Modifier.height(16.dp))
        
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
                    text = "🌱 Módulo 6",
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
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Resultados del análisis de alimentos con métricas de sostenibilidad",
                    color = colors.onPrimaryContainer.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Tabla responsiva mejorada
        if (isTablet || isLandscape) {
            // Vista de tabla completa para tablets y landscape
            TablaCompleta(resultados = resultados, colors = colors)
        } else {
            // Vista de tarjetas para móviles
            VistaTarjetas(resultados = resultados, colors = colors)
        }
        
        Spacer(Modifier.height(24.dp))
        
        // Resumen de resultados
        ResumenResultados(resultados = resultados, colors = colors)
        
        Spacer(Modifier.height(24.dp))
        
        // Botones de acción mejorados
        BotonesAccion(
            onBack = onBack,
            onPrint = onPrint,
            onSendEmail = onSendEmail,
            onNext = {
                val alimentosConSostenibilidad = viewModel.actualizarAlimentosConSostenibilidad(resultados)
                viewModel.updateAlimentos(alimentosConSostenibilidad)
                onNext()
            },
            colors = colors
        )
        
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun TablaCompleta(resultados: List<Alimento>, colors: ColorScheme) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        LazyColumn(
            modifier = Modifier.heightIn(max = 600.dp)
        ) {
            // Encabezados
            item {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(colors.primaryContainer)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🌾 Alimento", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                    Text("📏 Dist.", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("🌍 Nivel", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                    Text("🛒 Modo", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                    Text("📊 Valor", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("🌱 CO₂", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("⭐ Sosten.", color = colors.onPrimaryContainer, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.5f), textAlign = TextAlign.Center)
                }
            }
            
            // Datos
            items(resultados) { alimento ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .background(colors.surface)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(alimento.nombre, color = colors.onSurface, modifier = Modifier.weight(2f), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("${alimento.km} km", color = colors.onSurface, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text(alimento.nivel, color = colors.onSurface, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                    Text(alimento.modo, color = colors.onSurface, modifier = Modifier.weight(1.2f), textAlign = TextAlign.Center)
                    Text(alimento.valorAcumulado.toString(), color = colors.onSurface, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("%.1f".format(alimento.huellaCarbono), color = colors.onSurface, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    IndicadorSostenibilidad(alimento.sostenibilidad, colors, modifier = Modifier.weight(1.5f))
                }
            }
        }
    }
}

@Composable
private fun VistaTarjetas(resultados: List<Alimento>, colors: ColorScheme) {
    LazyColumn(
        modifier = Modifier.heightIn(max = 600.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(resultados) { alimento ->
            TarjetaAlimento(alimento = alimento, colors = colors)
        }
    }
}

@Composable
private fun TarjetaAlimento(alimento: Alimento, colors: ColorScheme) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header de la tarjeta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = alimento.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IndicadorSostenibilidad(alimento.sostenibilidad, colors)
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Detalles del alimento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetalleAlimento("📏 Distancia", "${alimento.km} km", colors)
                DetalleAlimento("🌍 Nivel", alimento.nivel, colors)
                DetalleAlimento("🛒 Modo", alimento.modo, colors)
            }
            
            Spacer(Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                DetalleAlimento("📊 Valor", alimento.valorAcumulado.toString(), colors)
                DetalleAlimento("🌱 CO₂", "%.1f".format(alimento.huellaCarbono), colors)
                DetalleAlimento("🚚 Transporte", alimento.transporte, colors)
            }
        }
    }
}

@Composable
private fun DetalleAlimento(label: String, value: String, colors: ColorScheme) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            color = colors.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = colors.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun IndicadorSostenibilidad(sostenibilidad: String, colors: ColorScheme, modifier: Modifier = Modifier) {
    val (color, icono) = when (sostenibilidad) {
        "Alta" -> Color(0xFF4CAF50) to "🟢"
        "Media" -> Color(0xFFFF9800) to "🟡"
        "Baja" -> Color(0xFFF44336) to "🔴"
        else -> colors.onSurface to "⚪"
    }
    
    Box(
        modifier = modifier
            .background(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                width = 2.dp,
                color = color,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(icono, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                sostenibilidad,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@Composable
private fun ResumenResultados(resultados: List<Alimento>, colors: ColorScheme) {
    val totalAlimentos = resultados.size
    val promedioValor = if (totalAlimentos > 0) resultados.map { it.valorAcumulado }.average() else 0.0
    val promedioCO2 = if (totalAlimentos > 0) resultados.map { it.huellaCarbono }.average() else 0.0
    val sostenibilidadAlta = resultados.count { it.sostenibilidad == "Alta" }
    val sostenibilidadMedia = resultados.count { it.sostenibilidad == "Media" }
    val sostenibilidadBaja = resultados.count { it.sostenibilidad == "Baja" }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colors.secondaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "📊 Resumen de Resultados",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colors.onSecondaryContainer,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ResumenItem("Total", totalAlimentos.toString(), "🌾", colors)
                ResumenItem("Prom. Valor", "%.1f".format(promedioValor), "📊", colors)
                ResumenItem("Prom. CO₂", "%.1f".format(promedioCO2), "🌱", colors)
            }
            
            Spacer(Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ResumenItem("Alta", sostenibilidadAlta.toString(), "🟢", colors)
                ResumenItem("Media", sostenibilidadMedia.toString(), "🟡", colors)
                ResumenItem("Baja", sostenibilidadBaja.toString(), "🔴", colors)
            }
        }
    }
}

@Composable
private fun ResumenItem(label: String, value: String, icon: String, colors: ColorScheme) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 20.sp)
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

@Composable
private fun BotonesAccion(
    onBack: () -> Unit,
    onPrint: () -> Unit,
    onSendEmail: () -> Unit,
    onNext: () -> Unit,
    colors: ColorScheme
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceVariant),
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("← Regresar", color = colors.onSurfaceVariant)
        }
        
        Spacer(Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onPrint,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("🖨️ Imprimir", color = colors.onPrimary)
            }
            
            Button(
                onClick = onSendEmail,
                colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("📧 Email", color = colors.onPrimary)
            }
        }
        
        Spacer(Modifier.height(12.dp))
        
        Button(
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Siguiente →", color = colors.onPrimary)
        }
    }
} 