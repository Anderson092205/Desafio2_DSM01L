package com.example.desafio2dsm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            returnToLogin()
            return
        }
        setContentView(R.layout.activity_welcome)
        findViewById<TextView>(R.id.welcomeUser).text = "Sesión: ${auth.currentUser?.email}"

        // Map button IDs to QuizRepository category IDs
        val categoryButtons = mapOf(
            R.id.generalKnowledgeButton to "el_salvador",
            R.id.scienceButton to "cultura_general",
            R.id.sportsButton to "ciencia",
            R.id.historyButton to "tecnologia"
        )

        categoryButtons.forEach { (buttonId, categoryId) ->
            findViewById<Button>(buttonId).setOnClickListener {
                selectedCategoryId = categoryId
                // Visual selection feedback: reset all, mark selected
                categoryButtons.keys.forEach { id ->
                    findViewById<Button>(id).isSelected = (id == buttonId)
                }
                val displayName = categoryDisplayName(categoryId)
                findViewById<TextView>(R.id.selectedCategoryText).text = "Tipo seleccionado: $displayName"
            }
        }

        findViewById<Button>(R.id.startQuizButton).setOnClickListener {
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

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            returnToLogin()
        }
    }

    private fun categoryDisplayName(id: String): String = when (id) {
        "el_salvador"   -> "El Salvador"
        "cultura_general" -> "Cultura General"
        "ciencia"       -> "Ciencia"
        "tecnologia"    -> "Tecnología"
        else            -> id
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
