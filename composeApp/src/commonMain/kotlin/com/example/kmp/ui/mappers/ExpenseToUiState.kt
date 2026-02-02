package com.example.kmp.ui.mappers

import com.example.kmp.model.Expense
import com.example.kmp.ui.details.DetailsUiState
import kotlin.math.abs

fun Expense.toUIState(): DetailsUiState {
    return DetailsUiState(
        amount = this.amount.toFormattedString(),
        description = this.description,
        category = this.category,
        isLoading = false
    )
}

/**
 * Formateador manual para KMP commonMain
 * Agrega separador de miles (.), decimales (,) y limita a 2 cifras.
 */
private fun Double.toFormattedString(): String {
    val stringPrefix = if (this < 0) "-" else ""
    val absoluteValue = abs(this)

    // Obtenemos la parte entera y decimal
    val parts = absoluteValue.toString().split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1].take(2).padEnd(2, '0') else "00"

    // Agregar separador de miles (.)
    val formattedInteger = integerPart.reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    return "$stringPrefix$formattedInteger,$decimalPart"
}