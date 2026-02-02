package com.example.kmp.data

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ExpenseManager {
    private var currentId = 6L // Empezamos después de los fakes

    // La clave de la reactividad: StateFlow
    private val _expenses = MutableStateFlow(initialFakeExpenses())
    val expenses = _expenses.asStateFlow()

    fun initialFakeExpenses() = listOf(
        Expense(1, 70.0, ExpenseCategory.GROCERIES, "Groceries"),
        Expense(2, 10.2, ExpenseCategory.TRANSPORTATION, "Transport"),
        Expense(3, 110.2, ExpenseCategory.CAR, "Car"),
        Expense(4, 102.2, ExpenseCategory.PARTY, "Party"),
        Expense(5, 102.2, ExpenseCategory.UNKNOWN, "Other")
    )

    fun addNewExpense(expense: Expense) {
        _expenses.update { currentList ->
            currentList + expense.copy(id = currentId++)
        }
    }

    fun editExpense(expense: Expense) {
        _expenses.update { currentList ->
            currentList.map { if (it.id == expense.id) expense else it }
        }
    }

    fun deleteExpense(id: Expense) {
        _expenses.update { currentList ->
            currentList.filter { it.id != id.id }
        }
    }

    fun getCategories(): List<ExpenseCategory> =
        listOf(
            ExpenseCategory.GROCERIES,
            ExpenseCategory.TRANSPORTATION,
            ExpenseCategory.FOOD,
            ExpenseCategory.PARTY,
            ExpenseCategory.COFFEE,
            ExpenseCategory.CAR,
            ExpenseCategory.HOUSE,
            ExpenseCategory.UNKNOWN
        )
   // fun getAllExpenses (): List<Expense> = initialFakeExpenses().sortedByDescending { it.id }.reversed()
    fun getExpenseById(id: Long): Expense? {
        return _expenses.value.find { it.id == id }
    }
}