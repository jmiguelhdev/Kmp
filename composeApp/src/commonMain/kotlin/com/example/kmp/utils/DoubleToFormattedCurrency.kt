package com.example.kmp.utils

/**
 * Extensión para formatear el total con 2 cifras y separador.
 * Nota: Reutilizamos la lógica que definimos para el Mapper de detalles.
 */
fun Double.toFormattedCurrency(): String {
    val absoluteValue = kotlin.math.abs(this)
    val parts = absoluteValue.toString().split(".")
    val integerPart = parts[0]
    val decimalPart = if (parts.size > 1) parts[1].take(2).padEnd(2, '0') else "00"

    // Formateo de miles simple para KMP Common
    val formattedInteger = integerPart.reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()

    return "$$formattedInteger,$decimalPart"
}