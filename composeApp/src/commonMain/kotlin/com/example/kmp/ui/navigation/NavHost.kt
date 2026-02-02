package com.example.kmp.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.ui.details.DetailsScreen
import com.example.kmp.ui.details.DetailsViewModel
import com.example.kmp.ui.expenses.ExpensesScreen
import com.example.kmp.ui.expenses.ExpensesViewModel

@Composable
fun AppNavHost(
    paddingValues: PaddingValues,
    navController: NavHostController,
    sharedNavigator: AppNavigator
) {


    NavHost(
        navController = navController,
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
        },
        modifier = Modifier.padding(paddingValues)
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
                    viewModel.onExpenseSelected(it.id)
                }
            )
        }
        composable<ScreenRoutes.ExpenseDetails> { backStackEntry ->
            // Recuperamos el ID que viene en la ruta
            val route: ScreenRoutes.ExpenseDetails = backStackEntry.toRoute()
            val detailsViewModel = viewModel<DetailsViewModel>(
                factory = viewModelFactory {
                    initializer {
                        DetailsViewModel(
                            expenseId = route.id,
                            repository = ExpenseRepoImpl(ExpenseManager),
                            navigator = sharedNavigator // El mismo que usa el AppNavHost
                        )
                    }
                }
            )

            DetailsScreen(
                id = route.id,
                viewModel = detailsViewModel
            )

        }
    }
    // Escuchamos los eventos de navegación globales
    LaunchedEffect(Unit) {
        sharedNavigator.navigationEvents.collect { action ->
            when (action) {
                is NavigationAction.NavigateTo -> {
                    navController.navigate(action.screen) {
                        // Si queremos limpiar la pila hasta una pantalla
                        action.popUpTo?.let { popScreen ->
                            popUpTo(popScreen) { inclusive = action.inclusive }
                        }
                        launchSingleTop = action.launchSingleTop
                    }
                }
                is NavigationAction.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

}