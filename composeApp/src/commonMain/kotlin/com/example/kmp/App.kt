package com.example.kmp

import AppTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.ui.expenses.ExpensesScreen
import com.example.kmp.ui.expenses.ExpensesViewModel

@Composable
fun App() {

    val viewModelProvider = viewModelFactory {
        initializer {
            ExpensesViewModel(ExpenseRepoImpl(ExpenseManager))
        }
    }
    val viewModel = viewModel<ExpensesViewModel>(factory = viewModelProvider)

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AppTheme {
        Surface {

                ExpensesScreen(
                    uiState = uiState,
                    onExpenseClick = {}
                )

        }
    }
}
