package com.example.kmp.domain.usecase

import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import kotlinx.coroutines.flow.Flow

class GetExpensesUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(): Flow<List<Expense>> = repository.getAllExpenses()
}