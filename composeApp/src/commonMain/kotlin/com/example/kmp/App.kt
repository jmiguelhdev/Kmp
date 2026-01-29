package com.example.kmp

import AppTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    AppTheme {
        Surface {
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxWidth()
            ) {
                Text("Bienvenidos")
                Text("Curso kmp")
            }
        }
    }
}



