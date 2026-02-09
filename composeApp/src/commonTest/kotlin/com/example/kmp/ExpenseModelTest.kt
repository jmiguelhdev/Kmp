package com.example.kmp

import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ExpenseModelTest {
    @Test
    fun testExpenseModel() {

        val expenseList = mutableListOf<Expense>()
        val expense = Expense(
            id = 1,
            amount = 100.0,
            category = ExpenseCategory.CAR,
            description = "combustible"
        )
        expenseList.add(expense)
        assertEquals(expenseList.size, 1)
        assertEquals(expenseList[0].amount , 100.0)
        assertEquals(expenseList[0].category , ExpenseCategory.CAR)
        assertEquals(expenseList[0].description , "combustible")
        assertContains(expenseList, expense)

    }
}