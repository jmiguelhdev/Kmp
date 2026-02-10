package com.example.kmp.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.ui.navigation.AppNavigator
import com.example.kmp.ui.navigation.ScreenRoutes
import com.example.kmp.utils.toFormattedCurrency
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0,
    val formattedTotal: String = "0.00"
)

class ExpensesViewModel(
    repository: ExpenseRepository,
    private val navigator: AppNavigator
) : ViewModel() {

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


    fun onExpenseSelected(id: Long) {
        navigator.navigateTo(ScreenRoutes.ExpenseDetails(id))
    }

}

