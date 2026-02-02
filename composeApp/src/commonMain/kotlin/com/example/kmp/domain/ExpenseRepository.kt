package com.example.kmp.domain

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun addExpense(expense: Expense)
    fun editExpense(expense: Expense)
    fun deleteExpense(expense: Expense)
    fun getCategories(): List<ExpenseCategory>
    fun getExpenseById(id: Long): Expense?
}

