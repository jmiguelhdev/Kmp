package com.example.kmp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.kmp.database.ExpenseDatabase
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.model.Expense
import com.example.kmp.model.ExpenseCategory
import com.example.kmp.model.ExpenseNetworkResponse
import com.example.kmp.ui.mappers.toNetworkRequest
import com.example.kmp.utils.log
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


private const val BASE_URL = "http://192.168.1.18:8080"

class ExpenseRepoImpl(
    private val expenseManager: ExpenseManager,
    database: ExpenseDatabase,
    private val httpClient: HttpClient
) : ExpenseRepository {

    private val queries = database.expensesDbQueries


    override suspend fun getAllExpenses(): Flow<List<Expense>> {
        return queries.selectAllExpenses()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { entities -> entities.map { it.toDomain() } }
    }


    override suspend fun syncExpenses() {
        Napier.log("syncExpenses: Intentando sincronizar...")
        try {
            val networkExpenses = httpClient.get("$BASE_URL/expenses")
                .body<List<ExpenseNetworkResponse>>()

            queries.transaction {
                networkExpenses.forEach { dto ->
                    queries.insertExpense(
                        id = dto.id,
                        amount = dto.amount,
                        categoryName = dto.category,
                        description = dto.description
                    )
                }
            }
            Napier.log("syncExpenses: Sincronización exitosa")
        } catch (e: Exception) {
            // Capturamos ConnectionRefused o cualquier error de red
            Napier.e("syncExpenses: Servidor no alcanzable. Usando datos locales.", throwable = e)
            // Lanzamos una excepción personalizada si el ViewModel necesita mostrar un mensaje específico
            throw e
        }
    }


    override suspend fun addExpense(expense: Expense) {
        Napier.log("addExpense: Intentando agregar gasto: $expense")
        try {
            // Convertimos dominio a DTO antes de enviar
            val request = expense.toNetworkRequest()
            val response = httpClient.post("$BASE_URL/expenses") {
                contentType(ContentType.Application.Json)
                setBody(request) // Enviamos el DTO serializable
            }

            if (response.status == HttpStatusCode.OK) {
                Napier.log("addExpense: API respondió OK. Guardando en DB local.: ${expense.id}")

                // Actualizar localmente si es necesario
                syncExpenses()
            } else {
                Napier.w("addExpense: La API respondió con un estado no esperado: ${response.status}")
            }
        } catch (e: Exception) {
            Napier.e("addExpense: Error", throwable = e)
        }
    }

    override suspend fun editExpense(expense: Expense) {
        Napier.log("editExpense: id=${expense.id}")
        try {
            // Convertimos dominio a DTO
            val request = expense.toNetworkRequest()

            val response = httpClient.put("$BASE_URL/expenses/${expense.id}") {
                contentType(ContentType.Application.Json)
                setBody(request) // Enviamos el DTO
            }

            if (response.status == HttpStatusCode.OK) {
                queries.updateExpense(
                    id = expense.id,
                    amount = expense.amount,
                    categoryName = expense.category.name,
                    description = expense.description
                )
                Napier.log("editExpense: OK")
            } else {
                Napier.w("editExpense: La API respondió con un estado no esperado: ${response.status}")
            }
        } catch (e: Exception) {
            Napier.e("editExpense: Falló", throwable = e)
        }
    }

    override suspend fun deleteExpense(expense: Expense) {
        Napier.log("deleteExpense: id=${expense.id}")
        try {
            // 1. Eliminar en API (Ktor)
            val response = httpClient.delete("$BASE_URL/expenses/${expense.id}")

            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.NotFound) {
                // 2. Eliminar localmente (SQLDelight)
                queries.transaction {
                    queries.deleteExpense(expense.id)
                }
                Napier.log("deleteExpense: Eliminado localmente")
            }
        } catch (e: Exception) {
            Napier.e("deleteExpense: Falló", throwable = e)
            throw e // Relanzamos para que el ViewModel pueda manejarlo si quiere
        }
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