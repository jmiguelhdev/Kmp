package com.example.kmp.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.kmp.database.ExpenseDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = ExpenseDatabase.Schema,
            name = "ExpenseDatabase.db"
        )
    }
}