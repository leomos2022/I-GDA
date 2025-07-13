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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
            val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

            // Leer preferencia al iniciar
            LaunchedEffect(Unit) {
                val prefs = context.dataStore.data.first()
                darkTheme = prefs[DARK_MODE_KEY] ?: false
            }

            CalculadoraAgroecologicaTheme(darkTheme = darkTheme) {
                val navController = rememberNavController()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {},
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
                                        contentDescription = if (darkTheme) "Modo claro" else "Modo oscuro"
                                    )
                                }
                            }
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
            }
        }
    }
}
