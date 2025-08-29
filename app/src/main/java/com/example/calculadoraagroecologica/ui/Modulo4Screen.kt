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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
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
            text = "Índice de Dependencia Alimentaria",
            color = colors.onBackground,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(32.dp))
        
        alimentos.forEachIndexed { i, alimento ->
            // Campo de distancia
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
            
            // Selector para Nivel de Origen
            var nivelOrigenExpanded by remember { mutableStateOf(false) }
            val nivelOrigenOpciones = listOf("LOCAL", "REGIONAL", "NACIONAL", "ZONAL", "MUNDIAL")
            
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = alimento.nivelOrigen,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Nivel de Origen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { nivelOrigenExpanded = true }
                )
                
                DropdownMenu(
                    expanded = nivelOrigenExpanded,
                    onDismissRequest = { nivelOrigenExpanded = false }
                ) {
                    nivelOrigenOpciones.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                alimentos[i] = alimento.copy(nivelOrigen = option)
                                nivelOrigenExpanded = false
                            }
                        )
                    }
                }
            }
            
            // Selector para Cuadrante de Recorrido
            var cuadranteExpanded by remember { mutableStateOf(false) }
            val cuadranteOpciones = listOf("CERCANO", "INTERMEDIO", "LEJANO", "MUY LEJANO")
            
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = alimento.cuadrante,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cuadrante de Recorrido") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { cuadranteExpanded = true }
                )
                
                DropdownMenu(
                    expanded = cuadranteExpanded,
                    onDismissRequest = { cuadranteExpanded = false }
                ) {
                    cuadranteOpciones.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                alimentos[i] = alimento.copy(cuadrante = option)
                                cuadranteExpanded = false
                            }
                        )
                    }
                }
            }
            
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