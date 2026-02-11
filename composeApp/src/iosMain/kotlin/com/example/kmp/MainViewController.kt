package com.example.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.kmp.di.initKoinIos
import com.example.kmp.utils.initNapier

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoinIos()
    }
) {
    initNapier()
    App()
}