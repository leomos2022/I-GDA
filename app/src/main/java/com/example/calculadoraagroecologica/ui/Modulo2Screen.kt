package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraagroecologica.ui.theme.EcoGreen
import com.example.calculadoraagroecologica.ui.theme.WhiteText
import com.example.calculadoraagroecologica.ui.theme.CharcoalGray
import com.example.calculadoraagroecologica.ui.theme.StoneGray
import com.example.calculadoraagroecologica.ui.CalculadoraViewModel
import com.example.calculadoraagroecologica.ui.model.Alimento
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun Modulo2Screen(viewModel: CalculadoraViewModel, onNext: () -> Unit) {
    val uiState by viewModel.uiState.collectAsState()
    var numAlimentos by remember { mutableStateOf(uiState.alimentos.size.coerceAtLeast(1)) }
    val alimentos = remember { mutableStateListOf<Alimento>().apply { addAll(uiState.alimentos.ifEmpty { List(1) { Alimento() } }) } }
    val colors = MaterialTheme.colorScheme

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
            text = "Módulo 2",
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
        
        OutlinedTextField(
            value = if (numAlimentos == 0) "" else numAlimentos.toString(),
            onValueChange = {
                val n = it.toIntOrNull() ?: 0
                numAlimentos = n
                if (n > 0) {
                    while (alimentos.size < n) alimentos.add(Alimento())
                    while (alimentos.size > n) alimentos.removeAt(alimentos.lastIndex)
                }
            },
            label = { Text("¿Cuántos alimentos deseas ingresar?", color = colors.onSurface) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = "Ingresa los nombres de los alimentos:",
            color = colors.onSurface,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(16.dp))
        
        // Lista de alimentos
        alimentos.forEachIndexed { i, alimento ->
            OutlinedTextField(
                value = alimento.nombre,
                onValueChange = { newName -> alimentos[i] = alimento.copy(nombre = newName) },
                label = { Text("Alimento #${i + 1}") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
        }
        
        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateAlimentos(alimentos.toList())
                onNext()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = EcoGreen,   // Fondo verde (o usa Color(0xFF4CAF50))
                contentColor   = WhiteText   // Letras blancas (o Color.White)
            ),
            enabled = alimentos.all { it.nombre.isNotBlank() },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Guardar y continuar")      // Ya no se necesita color aquí
        }
        
        Spacer(Modifier.height(32.dp))
    }
} 