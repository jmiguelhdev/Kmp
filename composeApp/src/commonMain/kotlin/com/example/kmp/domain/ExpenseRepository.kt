package com.example.kmp.domain

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory

interface ExpenseRepository {
    fun getAllExpenses(): List<Expense>
    fun addExpense(expense: Expense)
    fun editExpense(expense: Expense)
    fun deleteExpense(expense: Expense)
    fun getCategories(): List<ExpenseCategory>
}

