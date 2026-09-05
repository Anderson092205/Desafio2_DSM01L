package com.example.desafio2dsm

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var selectedCategoryId: String? = null

    // Map button IDs to QuizRepository category IDs
    private val categoryButtons = mapOf(
        R.id.generalKnowledgeButton to "el_salvador",
        R.id.scienceButton          to "cultura_general",
        R.id.sportsButton           to "ciencia",
        R.id.historyButton          to "tecnologia"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            returnToLogin()
            return
        }
        setContentView(R.layout.activity_welcome)
        findViewById<TextView>(R.id.welcomeUser).text = "Sesión: ${auth.currentUser?.email}"

        // Set up category buttons
        categoryButtons.forEach { (buttonId, categoryId) ->
            val btn = findViewById<MaterialButton>(buttonId)
            btn.setOnClickListener {
                selectedCategoryId = categoryId
                updateCategorySelection(buttonId)
                val displayName = categoryDisplayName(categoryId)
                findViewById<TextView>(R.id.selectedCategoryText).text = "Tipo seleccionado: $displayName"
            }
        }

        // Start quiz
        findViewById<MaterialButton>(R.id.startQuizButton).setOnClickListener {
            val catId = selectedCategoryId
            if (catId == null) {
                Toast.makeText(this, "Selecciona un tipo de quiz.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val difficulty = when (findViewById<RadioGroup>(R.id.difficultyGroup).checkedRadioButtonId) {
                R.id.hardRadio -> "Dificil"
                else -> "Facil"
            }
            startActivity(Intent(this, QuizActivity::class.java).apply {
                putExtra(EXTRA_CATEGORY, catId)
                putExtra(EXTRA_DIFFICULTY, difficulty)
            })
        }

        // Logout
        findViewById<MaterialButton>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            returnToLogin()
        }
    }

    /**
     * Visually highlights the selected category button (filled)
     * and resets the rest to outlined style.
     */
    private fun updateCategorySelection(selectedButtonId: Int) {
        categoryButtons.keys.forEach { buttonId ->
            val btn = findViewById<MaterialButton>(buttonId)
            val isSelected = (buttonId == selectedButtonId)

            if (isSelected) {
                // Filled: color de fondo primario, texto blanco
                btn.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.primary)
                )
                btn.setTextColor(ContextCompat.getColor(this, R.color.white))
                btn.strokeWidth = 0
            } else {
                // Outlined: fondo transparente, texto primario
                btn.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, android.R.color.transparent)
                )
                btn.setTextColor(ContextCompat.getColor(this, R.color.primary))
                btn.strokeWidth = 2
                btn.strokeColor = ColorStateList.valueOf(
                    ContextCompat.getColor(this, R.color.primary)
                )
            }
        }
    }

    private fun categoryDisplayName(id: String): String = when (id) {
        "el_salvador"     -> "El Salvador"
        "cultura_general" -> "Cultura General"
        "ciencia"         -> "Ciencia"
        "tecnologia"      -> "Tecnología"
        else              -> id
    }

    private fun returnToLogin() {
        startActivity(Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    companion object {
        const val EXTRA_CATEGORY = "EXTRA_CATEGORY"
        const val EXTRA_DIFFICULTY = "EXTRA_DIFFICULTY"
    }
}
