package com.example.kmp.ui.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ExpensesUiState(
    val expenses: List<Expense> = emptyList(),
    val total: Double = 0.0
)

class ExpensesViewModel(
    private val repository: ExpenseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpensesUiState())
    val uiState: StateFlow<ExpensesUiState> = _uiState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExpensesUiState()
    )

   // private val allExpenses = repository.getAllExpenses()

    init {
        getAllExpenses()
    }

    private fun updateState() {
        _uiState.value = _uiState.value.copy(
            expenses = repository.getAllExpenses(),
            total = repository.getAllExpenses().sumOf { it.amount }
        )
    }

    private fun getAllExpenses() {
        viewModelScope.launch {
            updateState()
        }
    }
    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            repository.addExpense(expense)
            updateState()
        }
    }
    fun editExpense(expense: Expense) {
        viewModelScope.launch {
            repository.editExpense(expense)
            updateState()
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
            updateState()
        }
    }

    fun getExpenseById(id: Long): Expense {
       return repository.getAllExpenses().first { it.id == id }
    }


}

