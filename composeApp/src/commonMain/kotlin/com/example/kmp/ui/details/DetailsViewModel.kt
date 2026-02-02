package com.example.kmp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.ExpenseCategory
import com.example.kmp.ui.mappers.toDomain
import com.example.kmp.ui.mappers.toUIState
import com.example.kmp.ui.navigation.AppNavigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsUiState(
    val amount: String = "",
    val description: String = "",
    val category: ExpenseCategory = ExpenseCategory.UNKNOWN,
    val isCategoryMenuExpanded: Boolean = false,
    val isLoading: Boolean = false
)

class DetailsViewModel(
    private val expenseId: Long,
    private val repository: ExpenseRepository,
    private val navigator: AppNavigator
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    val isNewExpense = expenseId == 0L

    init {
        // Regla de Oro: Si no es nuevo, recuperamos la información del repositorio al inicializar
        if (!isNewExpense) {
            loadExpense()
        }
    }

    private fun loadExpense() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val expense = repository.getExpenseById(expenseId)

            expense?.let { expense ->
                // USAMOS EL MAPPER DE DOMAIN A UI
                _uiState.value = expense.toUIState()
            } ?: run {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Funciones para actualizar el estado (Uso de Clean UI Pattern)
    fun onAmountChange(newValue: String) {
        _uiState.update { it.copy(amount = newValue) }
    }

    fun onDescriptionChange(newValue: String) {
        _uiState.update { it.copy(description = newValue) }
    }

    fun onCategoryChange(newCategory: ExpenseCategory) {
        _uiState.update { it.copy(category = newCategory, isCategoryMenuExpanded = false) }
    }

    fun toggleCategoryMenu(expanded: Boolean) {
        _uiState.update { it.copy(isCategoryMenuExpanded = expanded) }
    }

    fun handleExpenseAction() {

        val expense = _uiState.value.toDomain(expenseId)


        viewModelScope.launch {
            if (isNewExpense) {
                repository.addExpense(expense)
            } else {
                repository.editExpense(expense)
            }
            navigator.navigateBack()
        }

    }
}


