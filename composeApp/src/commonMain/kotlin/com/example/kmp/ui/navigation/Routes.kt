package com.example.kmp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoutes {
    @Serializable
    data object ExpensesList : ScreenRoutes()
    @Serializable
    data class ExpenseDetails(val id: Long) : ScreenRoutes()
}

