package com.example.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio2dsm.auth.AuthValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var progress: ProgressBar
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            openWelcome()
            return
        }
        setContentView(R.layout.activity_register)
        emailLayout = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        progress = findViewById(R.id.progressRegister)
        registerButton = findViewById(R.id.registerButton)

        registerButton.setOnClickListener { register() }
        findViewById<TextView>(R.id.goToLogin).setOnClickListener { finish() }
    }

    private fun register() {
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val password = passwordInput.text?.toString().orEmpty()
        val confirmation = confirmPasswordInput.text?.toString().orEmpty()
        emailLayout.error = AuthValidation.emailError(email)
        passwordLayout.error = AuthValidation.passwordError(password)
        confirmPasswordLayout.error = if (confirmation != password) "Las contraseñas no coinciden." else null
        if (emailLayout.error != null || passwordLayout.error != null || confirmPasswordLayout.error != null) return

        setLoading(true)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            setLoading(false)
            if (task.isSuccessful) {
                Toast.makeText(this, "Cuenta creada correctamente.", Toast.LENGTH_SHORT).show()
                openWelcome()
            } else {
                val message = when (task.exception) {
                    is FirebaseAuthUserCollisionException -> "Ya existe una cuenta con este correo."
                    is FirebaseAuthWeakPasswordException -> "La contraseña es demasiado débil."
                    else -> "No fue posible crear la cuenta. Verifica tu conexión e inténtalo de nuevo."
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        registerButton.isEnabled = !loading
    }

    private fun openWelcome() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finishAffinity()
    }
}
