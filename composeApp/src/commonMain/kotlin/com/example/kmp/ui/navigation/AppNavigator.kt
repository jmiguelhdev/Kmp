package com.example.kmp.ui.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppNavigator {
    private val _navigationEvents = MutableSharedFlow<ScreenRoutes>(extraBufferCapacity = 1)
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun navigateTo(screen: ScreenRoutes) {
        _navigationEvents.tryEmit(screen)
    }
}