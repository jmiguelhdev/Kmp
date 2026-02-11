package com.example.kmp.domain

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun getAllExpenses(): Flow<List<Expense>>
    suspend fun addExpense(expense: Expense)
    suspend fun editExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    fun getCategories(): List<ExpenseCategory>
    fun getExpenseById(id: Long): Expense?
}

