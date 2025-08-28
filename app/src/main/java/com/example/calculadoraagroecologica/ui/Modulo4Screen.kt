package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
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
fun Modulo4Screen(
    viewModel: CalculadoraViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPrint: () -> Unit,
    onSendEmail: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val tablas = uiState.tablas
    val alimentos = remember { mutableStateListOf<Alimento>().apply { addAll(uiState.alimentos) } }
    val colors = MaterialTheme.colorScheme
    val headerColor = colors.primary

    fun clasificarAlimento(km: Float): Pair<String, String> {
        for ((nivel, datos) in tablas.toMap()) {
            for ((cat, valKm) in datos) {
                if (km >= valKm) {
                    return nivel to cat
                }
            }
        }
        return "No definido" to "No definido"
    }

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
            text = "Módulo 4",
            color = colors.onBackground,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Registro de kilómetros por alimento",
            color = colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(32.dp))
        
        alimentos.forEachIndexed { i, alimento ->
            OutlinedTextField(
                value = if (alimento.km == 0f) "" else alimento.km.toString(),
                onValueChange = { newKm ->
                    val km = newKm.toFloatOrNull() ?: 0f
                    val (nivel, categoria) = clasificarAlimento(km)
                    alimentos[i] = alimento.copy(km = km, nivel = nivel, categoria = categoria)
                },
                label = { Text("Km de '${alimento.nombre}'") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Text(
                text = "Nivel: ${alimentos[i].nivel} | Categoría: ${alimentos[i].categoria}",
                color = CharcoalGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
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
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Siguiente", color = colors.onPrimary)
        }
        
        Spacer(Modifier.height(32.dp))
    }
}

// Extensión para convertir Tablas a Map<String, Map<String, Float>>
fun com.example.calculadoraagroecologica.ui.model.Tablas.toMap(): Map<String, Map<String, Float>> = mapOf(
    "Mundial" to mundial,
    "Continental" to continental,
    "Nacional" to nacional,
    "Regional" to regional,
    "Zonal" to zonal,
    "Local" to local
) 