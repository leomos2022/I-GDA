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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraagroecologica.ui.theme.EcoGreen
import com.example.calculadoraagroecologica.ui.theme.WhiteText
import com.example.calculadoraagroecologica.ui.theme.CharcoalGray
import com.example.calculadoraagroecologica.ui.theme.StoneGray
import com.example.calculadoraagroecologica.ui.CalculadoraViewModel
import com.example.calculadoraagroecologica.ui.model.Tablas
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.font.FontWeight
import java.text.DecimalFormat

@Composable
fun Modulo3Screen(
    viewModel: CalculadoraViewModel,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPrint: () -> Unit,
    onSendEmail: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val pd = uiState.pais.pd
    var tablas by remember { mutableStateOf(uiState.tablas) }
    var tablasCalculadas by remember { mutableStateOf(false) }
    val colors = MaterialTheme.colorScheme
    val tableBg = Color(0xFF23242A)
    val headerBg = Color(0xFF2C2D33)
    val borderColor = Color(0xFF44454A)
    val df = DecimalFormat("#,###.##")

    fun calcularTablas(pd: Float): Tablas {
        val mundial = mapOf(
            "Muy lejano" to 12000f,
            "Lejano" to 10000f,
            "Intermedio" to 8000f,
            "Cercano" to 6000f
        )
        val continental = mapOf(
            "Muy lejano" to 6000f,
            "Lejano" to 4000f,
            "Intermedio" to 3000f,
            "Cercano" to 2000f
        )
        val nacional = mapOf(
            "Muy lejano" to pd,
            "Lejano" to (0.6f * pd),
            "Intermedio" to (0.3f * pd),
            "Cercano" to (0.1f * pd)
        )
        val nacionalCercano = nacional["Cercano"] ?: 0f
        val regional = mapOf(
            "Muy lejano" to nacionalCercano,
            "Lejano" to (0.7f * nacionalCercano),
            "Intermedio" to (0.5f * nacionalCercano),
            "Cercano" to (0.3f * nacionalCercano)
        )
        val regionalCercano = regional["Cercano"] ?: 0f
        val zonal = mapOf(
            "Muy lejano" to regionalCercano,
            "Lejano" to (0.8f * regionalCercano),
            "Intermedio" to (0.6f * regionalCercano),
            "Cercano" to (0.4f * regionalCercano)
        )
        val local = mapOf(
            "Muy lejano" to regionalCercano,
            "Lejano" to (0.6f * regionalCercano),
            "Intermedio" to (0.4f * regionalCercano),
            "Cercano" to (0.2f * regionalCercano)
        )
        return Tablas(mundial, continental, nacional, regional, zonal, local)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18191C))
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = "Módulo 3: Índice de Dependencia Alimentaria",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                tablas = calcularTablas(pd)
                viewModel.updateTablas(tablas)
                tablasCalculadas = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            enabled = !tablasCalculadas,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Calcular tablas", color = colors.onPrimary)
        }
        Spacer(Modifier.height(24.dp))
        if (tablasCalculadas) {
            tablas.toMap().forEach { (nivel, datos) ->
                Spacer(Modifier.height(16.dp))
                // Título de la tabla
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\uD83D\uDCCA Tabla ${nivel}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                // Card de la tabla
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = tableBg),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(Modifier.padding(0.dp)) {
                        // Encabezado
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(headerBg)
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "",
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFBDBDBD),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Kilómetros",
                                modifier = Modifier.weight(1f),
                                color = Color(0xFFBDBDBD),
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                textAlign = TextAlign.End
                            )
                        }
                        // Filas
                        datos.forEach { (categoria, valor) ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(tableBg),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = categoria,
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = df.format(valor),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
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
            onClick = onNext,
            colors = ButtonDefaults.buttonColors(containerColor = colors.primary),
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text("Siguiente", color = colors.onPrimary)
        }
        Spacer(Modifier.height(32.dp))
    }
}