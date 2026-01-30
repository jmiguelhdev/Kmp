package com.example.kmp.data

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory

object ExpenseManager {
    private var currentId = 1L
    val fakeExpenseList = mutableListOf<Expense>(
        Expense(
            id = currentId++,
            amount = 70.0,
            category = ExpenseCategory.GROCERIES,
            description = ExpenseCategory.GROCERIES.name
        ),
        Expense(
            id = currentId++,
            amount = 10.2,
            category = ExpenseCategory.TRANSPORTATION,
            description = ExpenseCategory.GROCERIES.name,
        ),
        Expense(
            id = currentId++,
            amount = 110.2,
            category = ExpenseCategory.CAR,
            description = ExpenseCategory.GROCERIES.name,
        ),
        Expense(
            id = currentId++,
            amount = 102.2,
            category = ExpenseCategory.PARTY,
            description = ExpenseCategory.GROCERIES.name,
        ),
        Expense(
            id = currentId++,
            amount = 102.2,
            category = ExpenseCategory.UNKNOWN,
            description = ExpenseCategory.GROCERIES.name,
        )
    )

    fun addNewExpense(expense: Expense) {
        fakeExpenseList.add(expense.copy(id = currentId++))
    }
    fun editExpense(expense: Expense) {
        val index = fakeExpenseList.indexOfFirst { it.id == expense.id }
        if (index != -1) {
            fakeExpenseList[index] = fakeExpenseList[index].copy(
                amount = expense.amount,
                category = expense.category,
                description = expense.description
            )
        }
    }
    fun deleteExpense(expense: Expense):List<Expense> {
        fakeExpenseList.removeAt(expense.id.toInt())
        return fakeExpenseList.sortedByDescending { it.id }.reversed()
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
    fun getAllExpenses (): List<Expense> = fakeExpenseList.sortedByDescending { it.id }.reversed()

}