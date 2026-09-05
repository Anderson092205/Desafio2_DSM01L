package com.example.desafio2dsm.auth

import android.util.Patterns

object AuthValidation {
    fun emailError(email: String): String? = when {
        email.isBlank() -> "Ingresa tu correo electrónico."
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Ingresa un correo electrónico válido."
        else -> null
    }

    fun passwordError(password: String): String? = when {
        password.isBlank() -> "Ingresa tu contraseña."
        password.length < 6 -> "La contraseña debe tener al menos 6 caracteres."
        else -> null
    }
}
