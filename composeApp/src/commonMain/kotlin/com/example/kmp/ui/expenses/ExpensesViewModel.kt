package com.example.kmp.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.ui.navigation.AppNavigator
import com.example.kmp.ui.navigation.ScreenRoutes
import com.example.kmp.utils.toFormattedCurrency
import io.github.aakira.napier.Napier
import kotlinx.coroutines.delay
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
    val isLoading: Boolean = false,
    val error: String? = null
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
            // 1. Lanzamos un job infinito de sincronización (Polling) cada 10 segundos
            // En una app pro, aquí usarías WebSockets o Firebase
            launch {
                while(true) {
                    try {
                        repository.syncExpenses()
                        _uiState.update { it.copy(error = null) } // Limpiar error si conecta
                    } catch (e: Exception) {
                        Napier.w("Fallo el polling de red $e")
                        _uiState.update { it.copy(error = "No connection to server. Working offline.") }

                    }
                    delay(10000) // Sincroniza cada 10 seg
                }
            }

            // 2. Escuchamos la DB Local (SQLDelight)
            // Como syncExpenses inserta en la DB, este collectLatest se disparará
            // automáticamente cada vez que haya datos nuevos del servidor
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


    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                // El repositorio ahora solo ejecuta la acción suspend
                repository.deleteExpense(expense)
                // No necesitamos actualizar el estado aquí,
                // el collectLatest de loadExpenses() lo hará solo cuando la DB cambie.
            } catch (e: Exception) {
                Napier.e("Error al eliminar gasto", throwable = e)
            }
        }
    }

}

