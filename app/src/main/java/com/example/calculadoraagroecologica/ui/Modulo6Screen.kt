package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

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

    val resultados = alimentos.map { alimento ->
        val n = nivelMap[alimento.nivel] ?: 0
        val c = catMap[alimento.categoria] ?: 0
        val m = modoMap[alimento.modo] ?: 0
        val valor = (n + c) - m
        alimento.copy(valorAcumulado = valor)
    }

    val colors = MaterialTheme.colorScheme
    val headerColor = if (colors.isLight()) colors.primary else colors.onBackground
    val isDark = !colors.isLight()
    val resultTextColor = if (isDark) Color.White else CharcoalGray
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
            text = "Módulo 6",
            color = colors.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Cálculo de valor acumulado por alimento",
            color = colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(32.dp))
        
        // Tabla de resultados
        Text(
            text = "Resultados del cálculo:",
            color = resultTextColor,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Encabezados de la tabla
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Alimento", color = resultTextColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text("Nivel", color = resultTextColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text("Categoría", color = resultTextColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text("Modo", color = resultTextColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Text("Valor", color = resultTextColor, fontSize = 14.sp, modifier = Modifier.weight(1f))
        }
        
        Spacer(Modifier.height(8.dp))
        
        // Datos de la tabla
        resultados.forEach { alimento ->
            Row(
                Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    alimento.nombre,
                    color = resultTextColor,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    alimento.nivel,
                    color = resultTextColor,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    alimento.categoria,
                    color = resultTextColor,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    alimento.modo,
                    color = resultTextColor,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    alimento.valorAcumulado.toString(),
                    color = resultTextColor,
                    fontSize = 12.sp,
                    modifier = Modifier.weight(1f)
                )
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
                viewModel.updateAlimentos(resultados)
                onNext()
            },
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Siguiente", color = colors.onPrimary)
        }
        
        Spacer(Modifier.height(32.dp))
    }
} 