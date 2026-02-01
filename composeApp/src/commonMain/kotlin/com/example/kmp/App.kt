package com.example.kmp

import AppTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.kmp.ui.navigation.AppNavHost
import com.example.kmp.ui.navigation.ScreenRoutes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun App() {
    // 1. Instanciamos el NavController aquí arriba para poder observarlo
    val navController = rememberNavController()

    // 2. Obtenemos la entrada actual de la pila de navegación
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 3. Calculamos si estamos agregando un gasto basándonos en la ruta
    // Usamos 'toRoute' para obtener los parámetros de forma Type-Safe
    val currentRoute = navBackStackEntry?.toRoute<ScreenRoutes>()

    val isAddExpense = when(currentRoute) {
        is ScreenRoutes.ExpenseDetails -> currentRoute.id == 0L
        is ScreenRoutes.AddExpense -> true
        else -> false
    }



    AppTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (isAddExpense) "Añadir Gasto" else "Mis Gastos",
                            fontSize = 25.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                //navigator.navigateBack()
                            }
                        ) {
                            Icon(
                                imageVector = if (isAddExpense) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .padding(16.dp),
                            )
                        }
                    }
                )
            },

        ) { paddingValues ->
            AppNavHost(paddingValues, navController)
        }
    }
}
