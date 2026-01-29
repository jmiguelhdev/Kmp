package com.example.kmp.ui.Expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun ExpensesScreen() {
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
                    Header(total = 100.0)
                    AllExpensesHeader()
                }
            }
            items(emptyList<String>()) {
                //ExpenseItem()
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
            color = Color.Black
        )
        Button(
            shape = RoundedCornerShape(50),
            onClick = {

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
            )
        ) {
            Text(text = "view all")
        }
    }
}

@Composable
fun Header(total: Double) {
    Card(
        shape = RoundedCornerShape(30),
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black
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
                text = "$$total",
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

