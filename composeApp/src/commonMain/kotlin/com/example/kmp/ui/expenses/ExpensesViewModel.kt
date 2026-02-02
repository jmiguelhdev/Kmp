package com.example.kmp.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.ui.navigation.AppNavigator
import com.example.kmp.ui.navigation.ScreenRoutes
import com.example.kmp.utils.toFormattedCurrency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0,
    val formattedTotal: String = "0.00"
)

class ExpensesViewModel(
    private val repository: ExpenseRepository, private val navigator: AppNavigator
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesUiState())
    // TRANSFORMACIÓN REACTIVA:
    // Convertimos el Flow del repositorio directamente al State de la UI
    val uiState: StateFlow<ExpensesUiState> = repository.getAllExpenses()
        .map { list ->
            val totalAmount = list.sumOf { it.amount }
            ExpensesUiState(
                expenses = list,
                total = totalAmount,
                formattedTotal = totalAmount.toFormattedCurrency()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ExpensesUiState()
        )



    init {
        updateState()
    }

    private fun updateState() {
        viewModelScope.launch {
            repository.getAllExpenses().collect { list ->
                _uiState.update { expenseUiState ->
                    expenseUiState.copy(
                        expenses = list,
                        total = list.sumOf { it.amount },
                        formattedTotal = list.sumOf { it.amount }.toFormattedCurrency()
                    )

                }
            }
        }
    }
    fun onExpenseSelected(id: Long) {
        navigator.navigateTo(ScreenRoutes.ExpenseDetails(id))
    }

}

