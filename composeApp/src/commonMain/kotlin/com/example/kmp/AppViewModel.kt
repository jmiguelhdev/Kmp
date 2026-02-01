package com.example.kmp

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
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

    fun updateRoute(destination: NavDestination?) {
        val title = when {
            destination?.hasRoute<ScreenRoutes.ExpensesList>() == true -> TitleTopBarType.BASHBOARD.value
            destination?.hasRoute<ScreenRoutes.AddExpense>() == true -> TitleTopBarType.ADD.value
            destination?.hasRoute<ScreenRoutes.EditExpense>() == true -> TitleTopBarType.EDIT.value
            destination?.hasRoute<ScreenRoutes.ExpenseDetails>() == true -> TitleTopBarType.DETAILS.value
            else -> TitleTopBarType.BASHBOARD.value
        }

        _uiState.update {
            it.copy(
                title = title,
                isDetailScreen = title != TitleTopBarType.BASHBOARD.value
            )
        }
    }
}