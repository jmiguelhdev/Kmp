package com.example.kmp.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

// Representa las acciones posibles de navegación
sealed class NavigationAction {
    data class NavigateTo(
        val screen: ScreenRoutes,
        val popUpTo: ScreenRoutes? = null,
        val inclusive: Boolean = false,
        val launchSingleTop: Boolean = true
    ) : NavigationAction()

    data object NavigateBack : NavigationAction()
}

class AppNavigator {
    private val _navigationEvents = MutableSharedFlow<NavigationAction>(extraBufferCapacity = 1)
    val navigationEvents = _navigationEvents.asSharedFlow()

    // Navegación estándar
    fun navigateTo(
        screen: ScreenRoutes,
        popUpTo: ScreenRoutes? = null,
        inclusive: Boolean = false
    ) {
        _navigationEvents.tryEmit(
            NavigationAction.NavigateTo(screen, popUpTo, inclusive)
        )
    }

    // Métod o para el botón "Atrás"
    fun navigateBack() {
        _navigationEvents.tryEmit(NavigationAction.NavigateBack)
    }
}