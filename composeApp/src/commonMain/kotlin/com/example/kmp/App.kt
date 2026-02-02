package com.example.kmp

import AppTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.kmp.ui.navigation.AppNavHost
import com.example.kmp.ui.navigation.AppNavigator
import com.example.kmp.ui.navigation.ScreenRoutes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App() {
    // 1. Instanciamos el NavController aquí arriba para poder observarlo
    val navController = rememberNavController()
    val sharedNavigator = remember { AppNavigator() }

    // 2. Obtenemos la entrada actual de la pila de navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val appViewModel = viewModel<AppViewModel> { AppViewModel() }
    val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()

    // Usamos un LaunchedEffect para notificar al ViewModel cada vez que cambia la ruta
    LaunchedEffect(navBackStackEntry) {
        val destination = navBackStackEntry?.destination

        // Intentamos obtener la ruta tipada solo si el destino coincide
        val route = try {
            if (destination?.hasRoute<ScreenRoutes.ExpenseDetails>() == true) {
                navBackStackEntry?.toRoute<ScreenRoutes.ExpenseDetails>()
            } else null
        } catch (e: Exception) { null }

        appViewModel.updateRoute(destination, route)
    }
    AppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = appUiState.title,
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                if (appUiState.isDetailScreen) {
                                    sharedNavigator.navigateBack()
                                } else {
                                    // Aquí podrías abrir un Drawer, mostrar un perfil o
                                    // simplemente no hacer nada si es el icono de branding
                                    // Napier.d("Click en icono de Dashboard - No se requiere navegación")
                                }
                            }
                        ) {
                            if (appUiState.isDetailScreen){
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.MenuOpen,
                                    contentDescription = "Dashboard Icon",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                if (!appUiState.isDetailScreen) {
                    FloatingActionButton(
                        onClick = {
                        sharedNavigator.navigateTo(ScreenRoutes.ExpenseDetails(id = 0L))
                    },
                        shape = RoundedCornerShape(30),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Expense"
                        )
                    }
                }
            }

        ) { paddingValues ->
            AppNavHost(paddingValues, navController, sharedNavigator)
        }
    }
}


