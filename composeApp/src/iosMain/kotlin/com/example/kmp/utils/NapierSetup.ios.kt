package com.example.kmp.utils

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

actual fun initNapier() {
    Napier.base(DebugAntilog())
}