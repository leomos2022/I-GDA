package com.example.calculadoraagroecologica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.calculadoraagroecologica.ui.*
import com.example.calculadoraagroecologica.ui.theme.CalculadoraAgroecologicaTheme
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign

val Context.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: CalculadoraViewModel by viewModels()
        enableEdgeToEdge()

        setContent {
            val scope = rememberCoroutineScope()
            val context = this
            var darkTheme by rememberSaveable { mutableStateOf(false) }
            var showMenu by rememberSaveable { mutableStateOf(false) }
            val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

            // Leer preferencia al iniciar
            LaunchedEffect(Unit) {
                val prefs = context.dataStore.data.first()
                darkTheme = prefs[DARK_MODE_KEY] ?: false
            }

            CalculadoraAgroecologicaTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                val colors = MaterialTheme.colorScheme
                
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { 
                                Text(
                                    "🌱 I-GDA Calculadora Agroecológica",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { showMenu = true }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_menu),
                                        contentDescription = "Menú de navegación",
                                        tint = colors.onPrimary
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    darkTheme = !darkTheme
                                    scope.launch {
                                        context.dataStore.edit { prefs ->
                                            prefs[DARK_MODE_KEY] = darkTheme
                                        }
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = if (darkTheme) R.drawable.ic_light_mode else R.drawable.ic_dark_mode),
                                        contentDescription = if (darkTheme) "Modo claro" else "Modo oscuro",
                                        tint = colors.onPrimary
                                    )
                                }
                            },
                            colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                                containerColor = colors.primary,
                                titleContentColor = colors.onPrimary,
                                navigationIconContentColor = colors.onPrimary,
                                actionIconContentColor = colors.onPrimary
                            )
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "splash",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash") {
                            SplashScreen(onFinish = { navController.navigate("modulo1") })
                        }
                        composable("modulo1") {
                            Modulo1Screen(
                                viewModel = viewModel,
                                onNext = { navController.navigate("modulo2") }
                            )
                        }
                        composable("modulo2") {
                            Modulo2Screen(
                                viewModel = viewModel,
                                onNext = { navController.navigate("modulo3") }
                            )
                        }
                        composable("modulo3") {
                            Modulo3Screen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNext = { navController.navigate("modulo4") },
                                onPrint = { /* Lógica de impresión */ },
                                onSendEmail = { /* Lógica de envío de correo */ }
                            )
                        }
                        composable("modulo4") {
                            Modulo4Screen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNext = { navController.navigate("modulo5") },
                                onPrint = { /* Lógica de impresión */ },
                                onSendEmail = { /* Lógica de envío de correo */ }
                            )
                        }
                        composable("modulo5") {
                            Modulo5Screen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNext = { navController.navigate("modulo6") },
                                onPrint = { /* Lógica de impresión */ },
                                onSendEmail = { /* Lógica de envío de correo */ }
                            )
                        }
                        composable("modulo6") {
                            Modulo6Screen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onNext = { navController.navigate("modulo7") },
                                onPrint = { /* Lógica de impresión */ },
                                onSendEmail = { /* Lógica de envío de correo */ }
                            )
                        }
                        composable("modulo7") {
                            Modulo7Screen(
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() },
                                onPrint = { /* Lógica de impresión */ },
                                onSendEmail = { /* Lógica de envío de correo */ },
                                navController = navController
                            )
                        }
                    }
                }
                
                // Menú hamburger desplegable mejorado
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier
                        .background(colors.surface)
                        .width(300.dp)
                ) {
                    // Encabezado del menú
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.primaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🌱 Módulos de la Aplicación",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Calculadora I-GDA para análisis agroecológico",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onPrimaryContainer.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    
                    Divider(color = colors.outline)
                    
                    // Módulos disponibles con mejor diseño
                    val modulos = listOf(
                        "🌍 Módulo 1: Ubicación del País" to "modulo1",
                        "🍽️ Módulo 2: Ingreso de alimentos" to "modulo2",
                        "📏 Módulo 3: Tablas de distancia" to "modulo3",
                        "🚚 Módulo 4: Distancias de transporte" to "modulo4",
                        "🛒 Módulo 5: Modos de adquisición" to "modulo5",
                        "📊 Módulo 6: Cálculo de valores" to "modulo6",
                        "📈 Módulo 7: Resultados finales" to "modulo7"
                    )
                    
                    modulos.forEach { (nombre, destino) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = nombre,
                                    color = colors.onSurface,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            onClick = {
                                showMenu = false
                                navController.navigate(destino) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            modifier = Modifier.background(colors.surface)
                        )
                    }
                    
                    Divider(color = colors.outline)
                    
                    // Información adicional
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.secondaryContainer),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ℹ️ Acerca de I-GDA",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Índice de Gestión de Distancia Agroecológica",
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSecondaryContainer.copy(alpha = 0.8f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
