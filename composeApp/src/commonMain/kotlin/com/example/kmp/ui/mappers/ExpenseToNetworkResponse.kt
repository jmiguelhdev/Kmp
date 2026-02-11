package com.example.kmp.ui.mappers

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import com.example.kmp.model.ExpenseNetworkRequest
import com.example.kmp.model.ExpenseNetworkResponse
import kotlinx.serialization.Serializable


// Dominio -> Request (Para POST/PUT)
fun Expense.toNetworkRequest(): ExpenseNetworkRequest = ExpenseNetworkRequest(
    id = if (id == -1L) null else id,
    amount = amount,
    category = category.name,
    description = description
)

// Response -> Dominio (Para GET)
fun ExpenseNetworkResponse.toDomain(): Expense = Expense(
    id = id,
    amount = amount,
    category = try { ExpenseCategory.valueOf(category) } catch (e: Exception) { ExpenseCategory.UNKNOWN },
    description = description
)