package com.example.kmp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kmp.model.ExpenseCategory

// /src/commonMain/kotlin/com/example/kmp/ui/details/DetailsScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    id: Long,
    viewModel: DetailsViewModel // Pasamos el VM directamente para observar el estado
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categories = ExpenseCategory.entries
    val sheetState = rememberModalBottomSheetState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Campo de Precio
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                prefix = {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true
            )

            // Campo de Descripción
            OutlinedTextField(
                value = uiState.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )


            // Selector de Categoría (Visualmente como un botón de selección)
            OutlinedTextField(
                value = uiState.category.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleCategoryMenu(true) },
                enabled = false, // Deshabilitamos escritura pero mantenemos estética
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.primary
                ),
                leadingIcon = {
                    Icon(uiState.category.icon, contentDescription = null)
                }
            )

            Spacer(modifier = Modifier.weight(1f))


            // Botón de Acción
            Button(
                onClick = { viewModel.handleExpenseAction() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = uiState.amount.isNotEmpty() && uiState.description.isNotEmpty()
            ) {
                Text(if (id == 0L) "Add Expense" else "Update Expense")
            }
        }
    }
    // --- MODAL BOTTOM SHEET PARA CATEGORÍAS ---
    if (uiState.isCategoryMenuExpanded) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.toggleCategoryMenu(false) },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.height(300.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(
                            category = category,
                            isSelected = uiState.category == category,
                            onClick = { viewModel.onCategoryChange(category) }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryItem(
    category: ExpenseCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .clickable { onClick() }
            .background(if (isSelected) MaterialTheme.colorScheme.onPrimaryFixedVariant else Color.Transparent)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.name,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = category.name,
            style = MaterialTheme.typography.labelMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.padding(top = 8.dp),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}