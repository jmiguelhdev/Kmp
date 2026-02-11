package com.example.kmp.di

import com.example.kmp.AppViewModel
import com.example.kmp.data.DatabaseDriverFactory
import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.database.ExpenseDatabase
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.ui.details.DetailsViewModel
import com.example.kmp.ui.expenses.ExpensesViewModel
import com.example.kmp.ui.navigation.AppNavigator
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import kotlin.collections.toTypedArray

// Capa DATA: Singletons para Repositorios y Fuentes de Datos
val dataModule = module {
    // 1. Proveer HttpClient configurado
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
        }
    }
    single { ExpenseManager() }
    single {
        val driver = get<DatabaseDriverFactory>().createDriver()
        ExpenseDatabase(driver)
    }
    single<ExpenseRepository> {
        ExpenseRepoImpl(
            get(),
            get(),
            get()
        )
    }
}

// Capa DOMAIN: Factory para UseCases (si tuvieras)
val domainModule = module {

}

// Capa UI: ViewModels y Navegación
val uiModule = module {
    single { AppNavigator() }
    viewModelOf(::AppViewModel)
    viewModelOf(::ExpensesViewModel)

    // Para el DetailsViewModel que recibe un ID por parámetro
    viewModel { parameters ->
        DetailsViewModel(
            expenseId = parameters.get(),
            repository = get(),
            navigator = get()
        )
    }
}

// 1. Declaramos que cada plataforma DEBE proveer su propio modulo
expect val platformModule: Module

// Función para inicializar Koin (Common para todas las plataformas)
// Necesitamos una forma de decirle a Koin qué módulos usar
// Agregamos este parámetro a initKoin
fun initKoin(platformModules: List<Module> = emptyList(), config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, uiModule, *platformModules.toTypedArray())
    }
}

// Helper específico para iOS (opcional, pero recomendado para Swift)
fun initKoinIos() = initKoin(listOf(platformModule))