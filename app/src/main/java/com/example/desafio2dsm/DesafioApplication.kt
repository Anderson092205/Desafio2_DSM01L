package com.example.desafio2dsm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/**
 * Application class to enforce system-wide DayNight behavior.
 * Ensures the app responds immediately and consistently to the device's
 * system dark mode setting across all Android versions.
 */
class DesafioApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
