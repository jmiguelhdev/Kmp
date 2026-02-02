package com.example.kmp

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.toRoute
import com.example.kmp.data.TitleTopBarType
import com.example.kmp.ui.navigation.ScreenRoutes
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppUiState(
    val title: String = TitleTopBarType.BASHBOARD.value,
    val isDetailScreen: Boolean = false
)

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState.asStateFlow()

    // Cambiamos la firma para no depender de Bundle
    fun updateRoute(destination: NavDestination?, route: ScreenRoutes?) {
        val title = when {
            destination?.hasRoute<ScreenRoutes.ExpensesList>() == true -> {
                TitleTopBarType.BASHBOARD.value
            }
            // Verificamos si la ruta es de tipo ExpenseDetails
            route is ScreenRoutes.ExpenseDetails -> {
                if (route.id == 0L) TitleTopBarType.ADD.value else TitleTopBarType.DETAILS.value
            }
            else -> TitleTopBarType.BASHBOARD.value
        }

        _uiState.update {
            it.copy(
                title = title,
                isDetailScreen = destination?.hasRoute<ScreenRoutes.ExpensesList>() == false
            )
        }
    }
}