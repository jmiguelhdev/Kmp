package com.example.kmp.data

import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory

class ExpenseRepoImpl(private val expenseManager: ExpenseManager): ExpenseRepository {
    override fun getAllExpenses(): List<Expense> = expenseManager.getAllExpenses()


    override fun addExpense(expense: Expense) {
        expenseManager.addNewExpense(expense)
    }

    override fun editExpense(expense: Expense) {
        expenseManager.editExpense(expense)
    }

    override fun deleteExpense(expense: Expense) {
        expenseManager.deleteExpense(expense)
    }

    override fun getCategories(): List<ExpenseCategory> {
        return expenseManager.getCategories()
    }
}