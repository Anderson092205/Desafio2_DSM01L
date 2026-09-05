package com.example.desafio2dsm

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2dsm.adapter.QuestionAdapter
import com.example.desafio2dsm.data.QuizRepository
import com.example.desafio2dsm.model.Question
import com.example.desafio2dsm.model.QuizCategory
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QuizActivity : AppCompatActivity() {

    private lateinit var rvQuestions: RecyclerView
    private lateinit var txtActiveQuizTitle: TextView
    private lateinit var txtActiveQuizSubtitle: TextView
    private lateinit var txtQuizProgressBadge: TextView
    private lateinit var txtDifficultyBadge: TextView
    private lateinit var btnResetQuiz: MaterialButton
    private lateinit var btnSubmitQuiz: MaterialButton

    private lateinit var questionAdapter: QuestionAdapter
    private var activeCategory: QuizCategory? = null
    private var difficulty: String = "Facil"
    private var questions: List<Question> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Read intent extras from WelcomeActivity
        val categoryName = intent.getStringExtra(WelcomeActivity.EXTRA_CATEGORY) ?: ""
        difficulty = intent.getStringExtra(WelcomeActivity.EXTRA_DIFFICULTY) ?: "Facil"

        // Resolve category from the name passed
        activeCategory = QuizRepository.getCategoryByName(categoryName)
            ?: QuizRepository.getCategories().firstOrNull()

        val cat = activeCategory ?: run {
            Toast.makeText(this, "No se encontró el quiz seleccionado.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load questions according to difficulty
        questions = QuizRepository.getQuestions(cat.id, difficulty)

        initViews()
        setupToolbar(cat)
        setupQuiz()
    }

    private fun initViews() {
        rvQuestions = findViewById(R.id.rvQuestions)
        txtActiveQuizTitle = findViewById(R.id.txtActiveQuizTitle)
        txtActiveQuizSubtitle = findViewById(R.id.txtActiveQuizSubtitle)
        txtQuizProgressBadge = findViewById(R.id.txtQuizProgressBadge)
        txtDifficultyBadge = findViewById(R.id.txtDifficultyBadge)
        btnResetQuiz = findViewById(R.id.btnResetQuiz)
        btnSubmitQuiz = findViewById(R.id.btnSubmitQuiz)
    }

    private fun setupToolbar(cat: QuizCategory) {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        txtActiveQuizTitle.text = cat.title
        txtActiveQuizSubtitle.text = if (difficulty == "Dificil") "Nivel: Difícil 🔥" else "Nivel: Fácil ⭐"
        txtDifficultyBadge.text = if (difficulty == "Dificil") "DIFÍCIL" else "FÁCIL"
        txtQuizProgressBadge.text = "0/${questions.size} Respondidas"
    }

    private fun setupQuiz() {
        questionAdapter = QuestionAdapter(questions) { answeredCount ->
            txtQuizProgressBadge.text = "$answeredCount/${questions.size} Respondidas"
        }

        rvQuestions.layoutManager = LinearLayoutManager(this)
        rvQuestions.adapter = questionAdapter

        btnResetQuiz.setOnClickListener { resetQuiz() }
        btnSubmitQuiz.setOnClickListener { validateAndSubmitQuiz() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun resetQuiz() {
        questionAdapter.resetAnswers()
        txtQuizProgressBadge.text = "0/${questions.size} Respondidas"
        rvQuestions.smoothScrollToPosition(0)
        Toast.makeText(this, "Todas las respuestas han sido borradas.", Toast.LENGTH_SHORT).show()
    }

    private fun validateAndSubmitQuiz() {
        val selectedAnswers = questionAdapter.getSelectedAnswers()

        // Find unanswered questions
        val missingQuestions = questions.filter { !selectedAnswers.containsKey(it.id) }

        if (missingQuestions.isNotEmpty()) {
            val missingIndices = missingQuestions.map { questions.indexOf(it) + 1 }
            val missingIds = missingQuestions.map { it.id }

            // Highlight missing questions
            questionAdapter.setMissingQuestions(missingIds)

            // Scroll to first missing question
            val firstMissingIndex = questions.indexOf(missingQuestions.first())
            rvQuestions.smoothScrollToPosition(firstMissingIndex)

            // Show validation alert
            showValidationAlertDialog(missingIndices)
        } else {
            // All answered — reveal results
            questionAdapter.revealResults()

            var correctCount = 0
            questions.forEach { q ->
                val userAns = selectedAnswers[q.id]
                if (userAns == q.correctAnswerIndex) correctCount++
            }

            showResultsDialog(correctCount, questions.size)
        }
    }

    private fun showValidationAlertDialog(missingNumbers: List<Int>) {
        val formattedNumbers = missingNumbers.joinToString(", ")
        MaterialAlertDialogBuilder(this)
            .setTitle("⚠️ Preguntas Incompletas")
            .setMessage(
                "Debes responder todas las preguntas antes de enviar el quiz.\n\n" +
                "Faltan por responder las preguntas: $formattedNumbers."
            )
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton("Entendido") { dialog, _ -> dialog.dismiss() }
            .setCancelable(true)
            .show()
    }

    private fun showResultsDialog(correctCount: Int, totalQuestions: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_results, null)

        val txtCategory = dialogView.findViewById<TextView>(R.id.txtResultCategory)
        val txtScorePercentage = dialogView.findViewById<TextView>(R.id.txtScorePercentage)
        val txtScoreFraction = dialogView.findViewById<TextView>(R.id.txtScoreFraction)
        val txtResultMessage = dialogView.findViewById<TextView>(R.id.txtResultMessage)
        val imgBadge = dialogView.findViewById<ImageView>(R.id.imgResultBadge)
        val btnReview = dialogView.findViewById<MaterialButton>(R.id.btnDialogReviewQuestions)
        val btnChange = dialogView.findViewById<MaterialButton>(R.id.btnDialogChangeQuiz)
        val btnReset = dialogView.findViewById<MaterialButton>(R.id.btnDialogResetQuiz)

        val percentage = if (totalQuestions > 0) (correctCount * 100) / totalQuestions else 0

        txtCategory.text = "Tipo de Quiz: ${activeCategory?.title ?: ""} · ${if (difficulty == "Dificil") "Difícil" else "Fácil"}"
        txtScorePercentage.text = "$percentage%"
        txtScoreFraction.text = "Obtuviste $correctCount de $totalQuestions respuestas correctas"

        // Feedback message based on correct count
        when (correctCount) {
            0, 1 -> {
                imgBadge.setImageResource(R.drawable.ic_warning)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.error))
                txtResultMessage.text = "vayase para la casa"
                txtScorePercentage.setTextColor(ContextCompat.getColor(this, R.color.error))
            }
            2, 3 -> {
                imgBadge.setImageResource(R.drawable.ic_warning)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.warning))
                txtResultMessage.text = "te falta estudiar"
                txtScorePercentage.setTextColor(ContextCompat.getColor(this, R.color.warning))
            }
            4 -> {
                imgBadge.setImageResource(R.drawable.ic_check)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.secondary))
                txtResultMessage.text = "entrando a pro"
                txtScorePercentage.setTextColor(ContextCompat.getColor(this, R.color.secondary))
            }
            else -> {
                // 5 or more correct (hard difficulty may have bonus)
                imgBadge.setImageResource(R.drawable.ic_check)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.success))
                txtResultMessage.text = if (difficulty == "Dificil") {
                    "eres un crack idolo mastodondte pecho peludo 🏆"
                } else {
                    "eres un crack idolo mastodondte pecho peludo"
                }
                txtScorePercentage.setTextColor(ContextCompat.getColor(this, R.color.success))
            }
        }

        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // "Ver respuestas" — dismiss dialog and scroll to top to review answers
        btnReview.setOnClickListener {
            alertDialog.dismiss()
            rvQuestions.smoothScrollToPosition(0)
        }

        // "Intentar de nuevo" — reset and start over
        btnReset.setOnClickListener {
            alertDialog.dismiss()
            resetQuiz()
        }

        // "Elegir otro quiz" — go back to WelcomeActivity
        btnChange.setOnClickListener {
            alertDialog.dismiss()
            startActivity(Intent(this, WelcomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            })
            finish()
        }

        alertDialog.show()
    }
}
