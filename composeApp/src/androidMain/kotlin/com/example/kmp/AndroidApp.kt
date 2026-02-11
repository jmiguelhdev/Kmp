package com.example.kmp

import android.app.Application
import com.example.kmp.di.initKoin
import com.example.kmp.di.platformModule
import com.example.kmp.utils.initNapier
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initNapier()
        initKoin(
            platformModules = listOf(platformModule)
        ) {
            androidContext(this@AndroidApp)
            androidLogger()

        }
    }
}