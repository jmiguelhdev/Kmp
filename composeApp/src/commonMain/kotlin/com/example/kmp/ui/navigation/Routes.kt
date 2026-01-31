package com.example.kmp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes {
    @Serializable
    data object ExpensesList : ScreenRoutes()
    @Serializable
    data object AddExpense : ScreenRoutes()
    @Serializable
    data object EditExpense : ScreenRoutes()
    @Serializable
    data class ExpenseDetails(val id: Long) : ScreenRoutes()
}

