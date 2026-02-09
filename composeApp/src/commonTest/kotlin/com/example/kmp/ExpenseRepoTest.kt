package com.example.kmp

import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ExpenseRepoTest {
    // Bajo Clean Architecture, testeamos el Repo usando el Manager (que actúa como in-memory DB)
    private lateinit var expenseManager: ExpenseManager
    private lateinit var repo: ExpenseRepoImpl

    @BeforeTest
    fun setup() {
        expenseManager = ExpenseManager() // Instancia fresca para cada test
        repo = ExpenseRepoImpl(expenseManager)

    }

    @Test
    fun `getAllExpenses should return flow with initial fake expenses`() = runTest {
        // When
        val expenses = repo.getAllExpenses().first() // Obtenemos la primera emisión del Flow

        // Then
        assertTrue(expenses.isNotEmpty())
        assertEquals(5, expenses.size) // Según tus fakes iniciales
    }

    @Test
    fun `addExpense should increase the list size and include the new expense`() = runTest {
        // Given
        val newExpense =
            Expense(amount = 99.9, category = ExpenseCategory.COFFEE, description = "Test Coffee")

        // When
        repo.addExpense(newExpense)
        val expenses = repo.getAllExpenses().first()

        // Then
        assertTrue(expenses.any { it.description == "Test Coffee" && it.amount == 99.9 })
    }

    @Test
    fun `editExpense should modify an existing expense in the manager`() = runTest {
        // Given
        val expensesBefore = repo.getAllExpenses().first()
        val expenseToEdit = expensesBefore.first()
        val updatedExpense =
            expenseToEdit.copy(description = "Modified Description", amount = 500.0)

        // When
        repo.editExpense(updatedExpense)
        val expensesAfter = repo.getAllExpenses().first()
        val result = expensesAfter.find { it.id == expenseToEdit.id }

        // Then
        assertNotNull(result)
        assertEquals("Modified Description", result.description)
        assertEquals(500.0, result.amount)
    }

    @Test
    fun `deleteExpense should remove the correct expense from the list`() = runTest {
        // Given
        val expensesBefore = repo.getAllExpenses().first()
        val expenseToDelete = expensesBefore.first()

        // When
        repo.deleteExpense(expenseToDelete)
        val expensesAfter = repo.getAllExpenses().first()

        // Then
        assertTrue(expensesAfter.none { it.id == expenseToDelete.id })
    }

    @Test
    fun `getExpenseById should return the specific expense or null if not found`() = runTest {
        // Given
        val idExistente = 1L
        val idInexistente = 999L

        // When
        val found = repo.getExpenseById(idExistente)
        val notFound = repo.getExpenseById(idInexistente)

        // Then
        assertNotNull(found, "el gasto con id $idExistente deberia existir en el manager")
        assertEquals(idExistente, found.id)
        assertNull(notFound, "Expense not found should return null${notFound?.id}")
    }

    @Test
    fun `getCategories should return the complete list of ExpenseCategory enum`() {
        // When
        val categories = repo.getCategories()

        // Then
        // Verificamos contra el enum de dominio directamente
        assertEquals(ExpenseCategory.entries.size, categories.size)
        assertTrue(categories.contains(ExpenseCategory.GROCERIES))
    }
}




