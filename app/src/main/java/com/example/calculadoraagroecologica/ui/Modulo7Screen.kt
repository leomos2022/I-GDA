package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.luminance
import androidx.navigation.NavController
import androidx.compose.ui.graphics.Color

@Composable
fun Modulo7Screen(
    viewModel: CalculadoraViewModel,
    onBack: () -> Unit,
    onPrint: () -> Unit,
    onSendEmail: () -> Unit,
    navController: NavController? = null // Permitir navegación al inicio
) {
    val uiState by viewModel.uiState.collectAsState()
    val alimentos = uiState.alimentos
    val totalValor = alimentos.sumOf { it.valorAcumulado }
    val promedioValor = if (alimentos.isNotEmpty()) totalValor / alimentos.size else 0
    val colors = MaterialTheme.colorScheme
    val headerColor = if (colors.isLight()) colors.primary else colors.onBackground
    val isDark = !colors.isLight()
    val resultTextColor = if (isDark) Color.White else CharcoalGray
    val igda = viewModel.calcularIGDA()
    val (indiceClasificacion, tipoAlimentacion) = viewModel.clasificacionIGDA()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top padding para centrar mejor el contenido
        Spacer(Modifier.height(32.dp))
        
        Text(
            text = "Módulo 7: Resultados finales",
            color = headerColor,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        // Fila alineada para índice y tipo de alimentación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Índice i-GDA",
                color = resultTextColor,
                fontSize = 16.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = "Tipo de alimentación:",
                color = resultTextColor,
                fontSize = 16.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${"%.2f".format(igda)}",
                color = EcoGreen,
                fontSize = 28.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = tipoAlimentacion,
                color = EcoGreen,
                fontSize = 20.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
        Spacer(Modifier.height(24.dp))
        
        Spacer(Modifier.height(32.dp))
        
        // Resumen de resultados
        Text(
            text = "Resumen de la huella agroecológica:",
            color = resultTextColor,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(24.dp))
        
        // Estadísticas
        Text(
            text = "📊 Estadísticas:",
            color = EcoGreen,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            text = "Total de alimentos: ${alimentos.size}",
            color = resultTextColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(8.dp))
        
        Text(
            text = "Valor total acumulado: $totalValor",
            color = resultTextColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(8.dp))
        
        Text(
            text = "Promedio por alimento: ${"%.2f".format(promedioValor.toDouble())}",
            color = resultTextColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(24.dp))
        
        // Interpretación
        Text(
            text = "📋 Interpretación:",
            color = EcoGreen,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        val interpretacion = when {
            promedioValor <= 2 -> "Excelente - Huella agroecológica muy baja"
            promedioValor <= 4 -> "Buena - Huella agroecológica baja"
            promedioValor <= 6 -> "Regular - Huella agroecológica moderada"
            else -> "Alta - Huella agroecológica elevada"
        }
        
        Text(
            text = interpretacion,
            color = resultTextColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
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
                navController?.navigate("modulo1") {
                    popUpTo("modulo1") { inclusive = true }
                    launchSingleTop = true
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Ir al inicio", color = colors.onPrimary)
        }
        Spacer(Modifier.height(32.dp))
    }
} 