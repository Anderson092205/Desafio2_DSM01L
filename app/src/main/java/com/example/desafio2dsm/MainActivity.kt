package com.example.desafio2dsm

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2dsm.adapter.QuestionAdapter
import com.example.desafio2dsm.adapter.QuizCategoryAdapter
import com.example.desafio2dsm.data.QuizRepository
import com.example.desafio2dsm.model.QuizCategory
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var layoutWelcome: View
    private lateinit var layoutQuizContainer: View
    private lateinit var rvCategories: RecyclerView
    private lateinit var rvQuestions: RecyclerView

    private lateinit var txtActiveQuizTitle: TextView
    private lateinit var txtActiveQuizSubtitle: TextView
    private lateinit var txtQuizProgressBadge: TextView

    private lateinit var btnBackToWelcome: ImageButton
    private lateinit var btnResetQuiz: MaterialButton
    private lateinit var btnSubmitQuiz: MaterialButton

    private lateinit var categoryAdapter: QuizCategoryAdapter
    private lateinit var questionAdapter: QuestionAdapter

    private var activeCategory: QuizCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupWelcomeScreen()
        setupQuizScreen()
    }

    private fun initViews() {
        layoutWelcome = findViewById(R.id.layoutWelcome)
        layoutQuizContainer = findViewById(R.id.layoutQuizContainer)
        rvCategories = findViewById(R.id.rvCategories)
        rvQuestions = findViewById(R.id.rvQuestions)

        txtActiveQuizTitle = findViewById(R.id.txtActiveQuizTitle)
        txtActiveQuizSubtitle = findViewById(R.id.txtActiveQuizSubtitle)
        txtQuizProgressBadge = findViewById(R.id.txtQuizProgressBadge)

        btnBackToWelcome = findViewById(R.id.btnBackToWelcome)
        btnResetQuiz = findViewById(R.id.btnResetQuiz)
        btnSubmitQuiz = findViewById(R.id.btnSubmitQuiz)
    }

    private fun setupWelcomeScreen() {
        val categories = QuizRepository.getCategories()
        categoryAdapter = QuizCategoryAdapter(categories) { category ->
            startQuiz(category)
        }
        rvCategories.layoutManager = LinearLayoutManager(this)
        rvCategories.adapter = categoryAdapter
    }

    private fun setupQuizScreen() {
        questionAdapter = QuestionAdapter(emptyList()) { answeredCount ->
            val total = activeCategory?.questions?.size ?: 0
            txtQuizProgressBadge.text = "$answeredCount/$total Respondidas"
        }

        rvQuestions.layoutManager = LinearLayoutManager(this)
        rvQuestions.adapter = questionAdapter

        btnBackToWelcome.setOnClickListener {
            showWelcomeScreen()
        }

        btnResetQuiz.setOnClickListener {
            resetQuiz()
        }

        btnSubmitQuiz.setOnClickListener {
            validateAndSubmitQuiz()
        }
    }

    private fun startQuiz(category: QuizCategory) {
        activeCategory = category
        txtActiveQuizTitle.text = category.title
        txtActiveQuizSubtitle.text = category.description
        txtQuizProgressBadge.text = "0/${category.questions.size} Respondidas"

        questionAdapter.setQuestions(category.questions)
        rvQuestions.scrollToPosition(0)

        layoutWelcome.visibility = View.GONE
        layoutQuizContainer.visibility = View.VISIBLE
    }

    private fun showWelcomeScreen() {
        layoutQuizContainer.visibility = View.GONE
        layoutWelcome.visibility = View.VISIBLE
        activeCategory = null
    }

    private fun resetQuiz() {
        val total = activeCategory?.questions?.size ?: 0
        questionAdapter.resetAnswers()
        txtQuizProgressBadge.text = "0/$total Respondidas"
        rvQuestions.smoothScrollToPosition(0)
        Toast.makeText(this, "Todas las respuestas han sido borradas.", Toast.LENGTH_SHORT).show()
    }

    private fun validateAndSubmitQuiz() {
        val category = activeCategory ?: return
        val selectedAnswers = questionAdapter.getSelectedAnswers()
        val questions = category.questions

        // Find unanswered questions
        val missingQuestions = questions.filter { !selectedAnswers.containsKey(it.id) }

        if (missingQuestions.isNotEmpty()) {
            val missingIndices = missingQuestions.map { questions.indexOf(it) + 1 }
            val missingIds = missingQuestions.map { it.id }

            // Highlight missing questions in the list
            questionAdapter.setMissingQuestions(missingIds)

            // Scroll to the first missing question
            val firstMissingIndex = questions.indexOf(missingQuestions.first())
            rvQuestions.smoothScrollToPosition(firstMissingIndex)

            // Show alert dialog informing which questions are missing
            showValidationAlertDialog(missingIndices)
        } else {
            // All questions answered -> calculate score and reveal answers
            questionAdapter.revealResults()

            var correctCount = 0
            questions.forEach { q ->
                val userAns = selectedAnswers[q.id]
                if (userAns == q.correctAnswerIndex) {
                    correctCount++
                }
            }

            showResultsDialog(correctCount, questions.size)
        }
    }

    private fun showValidationAlertDialog(missingNumbers: List<Int>) {
        val formattedNumbers = missingNumbers.joinToString(", ")

        MaterialAlertDialogBuilder(this)
            .setTitle("⚠️ Preguntas Incompletas")
            .setMessage("Debes responder todas las preguntas antes de enviar el quiz.\n\nFaltan por responder las preguntas: $formattedNumbers.")
            .setIcon(R.drawable.ic_warning)
            .setPositiveButton("Entendido") { dialog, _ ->
                dialog.dismiss()
            }
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

        val percentage = (correctCount * 100) / totalQuestions

        txtCategory.text = "Tipo de Quiz: ${activeCategory?.title ?: ""}"
        txtScorePercentage.text = "$percentage%"
        txtScoreFraction.text = "Obtuviste $correctCount de $totalQuestions respuestas correctas"

        when (correctCount) {
            0, 1 -> {
                imgBadge.setImageResource(R.drawable.ic_warning)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.error))
                txtResultMessage.text = "vayase para la casa"
            }
            2, 3 -> {
                imgBadge.setImageResource(R.drawable.ic_warning)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.warning))
                txtResultMessage.text = "te falta estudiar"
            }
            4 -> {
                imgBadge.setImageResource(R.drawable.ic_check)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.secondary))
                txtResultMessage.text = "entrando a pro"
            }
            else -> {
                imgBadge.setImageResource(R.drawable.ic_check)
                imgBadge.setColorFilter(ContextCompat.getColor(this, R.color.success))
                txtResultMessage.text = "eres un crack idolo mastodondte pecho peludo"
            }
        }

        val alertDialog = MaterialAlertDialogBuilder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        btnReview.setOnClickListener {
            alertDialog.dismiss()
            rvQuestions.smoothScrollToPosition(0)
        }

        btnChange.setOnClickListener {
            alertDialog.dismiss()
            showWelcomeScreen()
        }

        btnReset.setOnClickListener {
            alertDialog.dismiss()
            resetQuiz()
        }

        alertDialog.show()
    }
}