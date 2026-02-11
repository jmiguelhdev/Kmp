package com.example.kmp.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.ui.navigation.AppNavigator
import com.example.kmp.ui.navigation.ScreenRoutes
import com.example.kmp.utils.toFormattedCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0,
    val formattedTotal: String = "0.00",
    val isLoading: Boolean = false
)

class ExpensesViewModel(
    private val repository: ExpenseRepository,
    private val navigator: AppNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesUiState(isLoading = true))
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            // Sincronizar con API (Backend Ktor)
            try {
                repository.syncExpenses()
            } catch (e: Exception) {
                // Manejar error de red opcionalmente
            }

            // Recolectar el Flow de la DB/Repositorio
            // Al ser suspend, lo llamamos dentro de la corrutina
            repository.getAllExpenses().collectLatest { expenses ->
                val totalAmount = expenses.sumOf { it.amount }
                _uiState.update { currentState ->
                    currentState.copy(
                        expenses = expenses,
                        total = totalAmount,
                        formattedTotal = totalAmount.toFormattedCurrency(),
                        isLoading = false
                    )
                }
            }
        }
    }



    fun onExpenseSelected(id: Long) {
        navigator.navigateTo(ScreenRoutes.ExpenseDetails(id))
    }

}

