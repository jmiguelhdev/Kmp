package com.example.kmp.di

import com.example.kmp.AppViewModel
import com.example.kmp.data.ExpenseManager
import com.example.kmp.data.ExpenseRepoImpl
import com.example.kmp.domain.ExpenseRepository
import com.example.kmp.ui.details.DetailsViewModel
import com.example.kmp.ui.expenses.ExpensesViewModel
import com.example.kmp.ui.navigation.AppNavigator
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

// Capa DATA: Singletons para Repositorios y Fuentes de Datos
val dataModule = module {
    single { ExpenseManager() }
    single<ExpenseRepository> { ExpenseRepoImpl(get()) }
}

// Capa DOMAIN: Factory para UseCases (si tuvieras)
val domainModule = module {
    // Ejemplo: factory { GetExpensesUseCase(get()) }
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

// Función para inicializar Koin (Common para todas las plataformas)
fun initKoin(config: (KoinAppDeclaration)? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, domainModule, uiModule)
    }
}
// Helper específico para iOS (opcional, pero recomendado para Swift)
fun initKoinIos() = initKoin()