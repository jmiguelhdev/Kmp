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
import kotlinx.coroutines.flow.first
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


    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                // Llamada suspendida al repositorio
                val expenses = repository.getAllExpenses().first()
                val totalAmount = expenses.sumOf { it.amount }

                _uiState.update {
                    it.copy(
                        expenses = expenses,
                        total = totalAmount,
                        formattedTotal = totalAmount.toFormattedCurrency(),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                // Manejar error (ej. enviar a un StateFlow de errores)
            }
        }
    }

    fun onExpenseSelected(id: Long) {
        navigator.navigateTo(ScreenRoutes.ExpenseDetails(id))
    }

}

