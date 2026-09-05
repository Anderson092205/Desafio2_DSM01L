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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var progress: ProgressBar
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        // Firebase conserva automáticamente la sesión mientras no se llame a signOut().
        if (auth.currentUser != null) {
            openWelcome()
            return
        }

        setContentView(R.layout.activity_login)
        emailLayout = findViewById(R.id.emailLayout)
        passwordLayout = findViewById(R.id.passwordLayout)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        progress = findViewById(R.id.progressLogin)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener { login() }
        findViewById<TextView>(R.id.goToRegister).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = emailInput.text?.toString()?.trim().orEmpty()
        val password = passwordInput.text?.toString().orEmpty()
        emailLayout.error = AuthValidation.emailError(email)
        passwordLayout.error = AuthValidation.passwordError(password)
        if (emailLayout.error != null || passwordLayout.error != null) return

        setLoading(true)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            setLoading(false)
            if (task.isSuccessful) {
                openWelcome()
            } else {
                val message = when (task.exception) {
                    is FirebaseAuthInvalidUserException -> "No existe una cuenta con este correo."
                    is FirebaseAuthInvalidCredentialsException -> "Correo o contraseña incorrectos."
                    else -> "No fue posible iniciar sesión. Verifica tu conexión e inténtalo de nuevo."
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        progress.visibility = if (loading) View.VISIBLE else View.GONE
        loginButton.isEnabled = !loading
    }

    private fun openWelcome() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
