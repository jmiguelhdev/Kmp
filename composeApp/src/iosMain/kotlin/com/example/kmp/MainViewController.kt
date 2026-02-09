package com.example.kmp

import androidx.compose.ui.window.ComposeUIViewController
import com.example.kmp.di.initKoinIos

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoinIos()
    }
) {
    App()
}