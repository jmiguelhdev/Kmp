package com.example.kmp

import androidx.compose.animation.core.copy
import com.example.kmp.data.ExpenseManager
import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue



@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseManagerTest {

    private lateinit var manager: ExpenseManager

    @BeforeTest
    fun setup() {
        // Como es un Object (Singleton), podrías necesitar resetearlo
        // o usar una instancia nueva si fuera una clase.
        manager = ExpenseManager()
    }

    @Test
    fun `addNewExpense should emit a new list containing the added expense`() = runTest {
        // Given
        val newExpense =
            Expense(amount = 50.0, category = ExpenseCategory.COFFEE, description = "Test Coffee")

        // When
        manager.addNewExpense(newExpense)

        // Then
        val currentExpenses = manager.expenses.first()
        assertTrue(currentExpenses.any { it.description == "Test Coffee" && it.amount == 50.0 })
    }

    @Test
    fun `editExpense should update existing expense data`() = runTest {
        // Given
        val initialList = manager.expenses.first()
        val expenseToEdit = initialList.first()
        val updatedExpense = expenseToEdit.copy(description = "Updated Description")

        // When
        manager.editExpense(updatedExpense)

        // Then
        val updatedList = manager.expenses.first()
        val result = updatedList.find { it.id == expenseToEdit.id }
        assertEquals("Updated Description", result?.description)
    }
}