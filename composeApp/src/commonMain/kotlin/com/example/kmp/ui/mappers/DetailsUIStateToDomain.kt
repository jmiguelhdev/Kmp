package com.example.kmp.ui.mappers

import com.example.kmp.model.Expense
import com.example.kmp.ui.details.DetailsUiState

fun DetailsUiState.toDomain(id: Long): Expense {
    return Expense(
        id = id,
        // Limpiamos el input antes de convertirlo
        amount = this.amount.replace(",", ".").toDoubleOrNull() ?: 0.0,
        description = this.description.trim().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        },
        category = this.category
    )
}