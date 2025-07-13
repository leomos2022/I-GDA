package com.example.calculadoraagroecologica.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.calculadoraagroecologica.R
import com.example.calculadoraagroecologica.ui.theme.EcoGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinish: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Imagen a pantalla completa
        Image(
            painter = painterResource(id = R.drawable.trigo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Texto con recuadro verde transparente detrás
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(EcoGreen.copy(alpha = 0.75f)) // verde 75 % opaco
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Text(
                text = "I‑GDA  •  Índice de Dependencia Alimentaria",
                color = Color.White,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,      // letras más gruesas
                    lineHeight = 32.sp                  // un poco de aire extra
                )
                // Si aún quieres más grosor, instala una fuente *variable* con eje weight
                // y declara: fontWeight = FontWeight(900)
            )
        }
    }

    // Timer para pasar al siguiente destino
    LaunchedEffect(Unit) {
        delay(2_000)  // 2 s
        onFinish()
    }
}
