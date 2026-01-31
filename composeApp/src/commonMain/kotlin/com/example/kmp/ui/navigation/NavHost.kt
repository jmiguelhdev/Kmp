package com.example.kmp.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.ui.expenses.ExpensesScreen
import com.example.kmp.ui.expenses.ExpensesViewModel

@Composable
fun AppNavHost() {

    val navHostController = rememberNavController()

    // El Navigator debería ser único para toda la app (Singleton manual por ahora)
    val sharedNavigator = remember { AppNavigator() }


    NavHost(
        navController = navHostController,
        startDestination = ScreenRoutes.ExpensesList,
         // TRANSICIONES GLOBALES
        enterTransition = {
            slideInHorizontally(initialOffsetX = { it }) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = { -it }) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        }
    ) {
        composable<ScreenRoutes.ExpensesList>{
            // EL VIEWMODEL SE INICIALIZA AQUÍ
            val viewModel = viewModel<ExpensesViewModel>(
                factory = viewModelFactory {
                    initializer {
                        ExpensesViewModel(
                            repository = ExpenseRepoImpl(ExpenseManager),
                            navigator = sharedNavigator
                        )
                    }
                }
            )

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            ExpensesScreen(
                uiState = uiState,
                onExpenseClick = {
                    navHostController.navigate(ScreenRoutes.ExpenseDetails(it.id))
                }
            )
        }
        composable<ScreenRoutes.ExpenseDetails> { backStackEntry ->
            // Recuperamos el ID que viene en la ruta
            val route: ScreenRoutes.ExpenseDetails = backStackEntry.toRoute()

            // Aquí inicializarías el DetailViewModel de la misma forma
            Text("Detalle del gasto con ID: ${route.id}")
        }
    }
    // Escuchamos los eventos de navegación globales
    LaunchedEffect(Unit) {
        sharedNavigator.navigationEvents.collect { screen ->
            navHostController.navigate(screen)
        }
    }
}