package com.example.kmp.utils

import io.github.aakira.napier.Napier

// /composeApp/src/commonMain/kotlin/com/example/kmp/utils/NapierSetup.kt
// Esperamos que cada plataforma provea su propia inicialización
expect fun initNapier()

// Helper para logs con tag automático
fun Napier.log(message: String) {
    this.d(tag = "KMP_Expenses_App", message = message)
}