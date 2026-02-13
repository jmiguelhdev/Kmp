package com.example.kmp.ui.expenses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kmp.data.ExpenseManager
import com.example.kmp.model.Expense


@Composable
fun ExpensesScreen(
    uiState: ExpensesUiState,
    onExpenseClick: (expense: Expense) -> Unit,
    onDeleteExpense: (expense: Expense) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { message->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Aceptar"
            )
            if (result == SnackbarResult.ActionPerformed || result == SnackbarResult.Dismissed) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->

        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            ) {
            // 1. Mostrar Cargando
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // 2. Mostrar Contenido si no está cargando
            else {
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stickyHeader {
                        Column(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.background
                                )
                        ) {
                            Header(total = uiState.formattedTotal)
                            AllExpensesHeader()
                        }
                    }

                    // CORRECCIÓN: Usar !uiState.expenses.isEmpty() o simplemente items()
                    if (uiState.expenses.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxHeight(0.5f).fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No expenses found", color = Color.Gray)
                            }
                        }
                    } else {
                        items(
                            items = uiState.expenses,
                            key = { it.id } // Requerido para que SwipeToDismiss sepa qué item animar
                        ) { expense ->

                            val dismissState = rememberSwipeToDismissBoxState(
                                SwipeToDismissBoxValue.Settled,
                                SwipeToDismissBoxDefaults.positionalThreshold
                            )
                            // 2. Usamos LaunchedEffect para reaccionar al cambio de estado (Modern Way)
                            LaunchedEffect(dismissState.currentValue) {
                                if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
                                    onDeleteExpense(expense)
                                    // Opcional: dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                                    // si quieres que el item regrese si no se borra instantáneamente
                                }
                            }

                            SwipeToDismissBox(
                                state = dismissState,
                                enableDismissFromStartToEnd = false, // Solo de derecha a izquierda
                                backgroundContent = { SwipeBackground(dismissState) },
                                content = { ExpensesItem(expense, onExpenseClick) }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
        Color.Red.copy(alpha = 0.8f)
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color, RoundedCornerShape(30))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Image(
            imageVector = Icons.Default.Delete,
            contentDescription = "Delete",
            colorFilter = ColorFilter.tint(Color.White)
        )
    }
}


@Preview
@Composable
fun ExpenseScreenPreview() {
    ExpensesScreen(
        uiState = ExpensesUiState(
            expenses = ExpenseManager().initialFakeExpenses(),
            total = 100.0
    ),
        onExpenseClick = {},
        onDeleteExpense = {}
    )
}

@Composable
fun ExpensesItem(expense: Expense, onExpenseClick: (expense: Expense) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp)
            .clickable { onExpenseClick(expense) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer// Forzado a negro en ambos modos
        ),
        shape = RoundedCornerShape(30)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(35),
                color = Color.Cyan
            ) {
                Image(
                    imageVector = expense.icon,
                    contentDescription = expense.description,
                    modifier = Modifier.padding(10.dp),
                    colorFilter = ColorFilter.tint(Color.White),
                    contentScale = ContentScale.Crop
                )


            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = expense.category.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.White // Forzado a blanco para contraste con fondo negro
                )
                Text(
                    text = expense.description,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = Color.White // Forzado a blanco
                )
            }
            Text(
                text = "Total $${expense.amount}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White // Forzado a blanco
            )
        }
    }
}

@Composable
fun AllExpensesHeader() {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "All Expenses",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground // Responsivo al tema
        )
        Button(
            shape = RoundedCornerShape(50),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
            )
        ) {
            Text(text = "view all", color = Color.Black)
        }
    }
}

@Composable
fun Header(total: String) {
    Card(
        shape = RoundedCornerShape(30),
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black // También podemos forzar el header a negro si se desea consistencia
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = total,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = "USD",
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}
