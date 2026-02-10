package com.example.kmp.data

import com.example.kmp.database.ExpenseEntity
import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory

fun ExpenseEntity.toDomain(): Expense = Expense(
    id = id,
    amount = amount,
    category = ExpenseCategory.valueOf(categoryName),
    description = description
)