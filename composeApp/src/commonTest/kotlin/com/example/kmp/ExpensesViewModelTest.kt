package com.example.kmp

import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.ui.expenses.ExpensesViewModel
import com.example.kmp.ui.navigation.AppNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ExpensesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ExpensesViewModel
    private val repo = ExpenseRepoImpl(
        ExpenseManager(),
        database = TODO(),
        httpClient = TODO()
    )
    private val navigator = AppNavigator()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Redirige el Main thread de UI a nuestro test dispatcher
        viewModel = ExpensesViewModel(repo, navigator)
    }

    @Test
    fun `uiState should reflect the total sum of expenses correctly`() = runTest {
        // El StateFlow del ViewModel ya debería tener los fakes del Manager
        val state = viewModel.uiState.value

        val expectedTotal = state.expenses.sumOf { it.amount }
        assertEquals(expectedTotal, state.total)
    }
}