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
    private var selectedCategory: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            returnToLogin()
            return
        }
        setContentView(R.layout.activity_welcome)
        findViewById<TextView>(R.id.welcomeUser).text = "Sesión: ${auth.currentUser?.email}"

        val categoryButtons = mapOf(
            R.id.generalKnowledgeButton to "Cultura general",
            R.id.scienceButton to "Ciencia",
            R.id.sportsButton to "Deportes",
            R.id.historyButton to "Historia"
        )
        categoryButtons.forEach { (id, category) ->
            findViewById<Button>(id).setOnClickListener {
                selectedCategory = category
                categoryButtons.keys.forEach { buttonId ->
                    findViewById<Button>(buttonId).isSelected = buttonId == id
                }
                findViewById<TextView>(R.id.selectedCategoryText).text = "Tipo seleccionado: $category"
            }
        }

        findViewById<Button>(R.id.startQuizButton).setOnClickListener {
            val category = selectedCategory
            if (category == null) {
                Toast.makeText(this, "Selecciona un tipo de quiz.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val difficulty = when (findViewById<RadioGroup>(R.id.difficultyGroup).checkedRadioButtonId) {
                R.id.hardRadio -> "Dificil"
                else -> "Facil"
            }
            // Contrato para la actividad que implementará la parte del quiz.
            // Se usa el nombre de clase para que esta parte compile aunque QuizActivity
            // todavía esté siendo creada por el integrante encargado del quiz.
            startActivity(Intent().setClassName(this, "$packageName.QuizActivity").apply {
                putExtra(EXTRA_CATEGORY, category)
                putExtra(EXTRA_DIFFICULTY, difficulty)
            })
        }

        findViewById<Button>(R.id.logoutButton).setOnClickListener {
            auth.signOut()
            returnToLogin()
        }
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
