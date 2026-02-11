package com.example.kmp.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.PartyMode
import androidx.compose.material.icons.filled.ViewCozy
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

data class Expense(
    val id: Long = -1,
    val amount: Double,
    val category: ExpenseCategory,
    val description: String
) {
    val icon = category.icon
}

@Serializable
data class ExpenseNetworkRequest(
    val id: Long? = null,
    val amount: Double,
    val category: String, // Usamos String para evitar problemas con Enums complejos en la API
    val description: String
)

@Serializable
data class ExpenseNetworkResponse(
    val id: Long,
    val amount: Double,
    val category: String,
    val description: String
)
enum class ExpenseCategory(val icon: ImageVector) {
    GROCERIES(Icons.Default.FoodBank),
    TRANSPORTATION(Icons.Default.DirectionsCar),
    FOOD(Icons.Default.Fastfood),
    PARTY(Icons.Default.PartyMode),
    COFFEE(Icons.Default.Coffee),
    CAR(Icons.Default.ElectricCar),
    HOUSE(Icons.Default.House),
    UNKNOWN(Icons.Default.ViewCozy),
}