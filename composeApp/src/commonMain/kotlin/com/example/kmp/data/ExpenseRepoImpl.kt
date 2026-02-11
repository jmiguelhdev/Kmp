package com.example.kmp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.kmp.database.ExpenseDatabase
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private const val BASE_URL = "http://192.168.1.18:8080"
class ExpenseRepoImpl(
    private val expenseManager: ExpenseManager,
    database: ExpenseDatabase,
    private val httpClient: HttpClient
) : ExpenseRepository {

    private val queries = database.expensesDbQueries


    override suspend fun getAllExpenses(): Flow<List<Expense>> =
        queries.selectAllExpenses()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { expenseEntities ->
                expenseEntities.map { it.toDomain() }
            }


    override suspend fun addExpense(expense: Expense) {
        queries.transaction {
            queries.insertExpense(
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description
            )
        }
    }

    override suspend fun editExpense(expense: Expense) {
        queries.transaction {
            queries.updateExpense(
                amount = expense.amount,
                categoryName = expense.category.name,
                description = expense.description,
                id = expense.id
            )
        }
    }

    override suspend fun deleteExpense(expense: Expense) {
        queries.deleteExpense(expense.id)
    }

    override fun getCategories(): List<ExpenseCategory> {
        return expenseManager.getCategories()
    }

    override fun getExpenseById(id: Long): Expense? {
        return queries.selectExpenseById(id)
            .executeAsOneOrNull()
            ?.toDomain()
    }

}