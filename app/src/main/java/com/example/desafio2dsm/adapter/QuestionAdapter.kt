package com.example.desafio2dsm.adapter

import android.content.res.ColorStateList
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2dsm.R
import com.example.desafio2dsm.model.Question
import com.google.android.material.card.MaterialCardView

class QuestionAdapter(
    private var questions: List<Question> = emptyList(),
    private val onAnswerSelectedListener: (selectedCount: Int) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    // Maps questionId -> selectedOptionIndex
    private val selectedAnswers = mutableMapOf<Int, Int>()

    // Set of question IDs that failed validation (unanswered)
    private val missingQuestionIds = mutableSetOf<Int>()

    // Mode flag to visualize results after quiz submission
    private var isResultsRevealed: Boolean = false

    fun setQuestions(newQuestions: List<Question>) {
        questions = newQuestions
        selectedAnswers.clear()
        missingQuestionIds.clear()
        isResultsRevealed = false
        notifyDataSetChanged()
    }

    fun getSelectedAnswers(): Map<Int, Int> = selectedAnswers

    fun resetAnswers() {
        selectedAnswers.clear()
        missingQuestionIds.clear()
        isResultsRevealed = false
        notifyDataSetChanged()
        onAnswerSelectedListener(0)
    }

    fun setMissingQuestions(missingIds: List<Int>) {
        missingQuestionIds.clear()
        missingQuestionIds.addAll(missingIds)
        isResultsRevealed = false
        notifyDataSetChanged()
    }

    fun clearMissingWarnings() {
        missingQuestionIds.clear()
        notifyDataSetChanged()
    }

    fun revealResults() {
        isResultsRevealed = true
        missingQuestionIds.clear()
        notifyDataSetChanged()
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardQuestion: MaterialCardView = itemView.findViewById(R.id.cardQuestion)
        val txtNumber: TextView = itemView.findViewById(R.id.txtQuestionNumber)
        val txtStatus: TextView = itemView.findViewById(R.id.txtQuestionStatus)
        val txtText: TextView = itemView.findViewById(R.id.txtQuestionText)
        val radioGroup: RadioGroup = itemView.findViewById(R.id.radioGroupOptions)
        val txtWarning: TextView = itemView.findViewById(R.id.txtWarningUnanswered)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questions[position]
        val context = holder.itemView.context

        // Title and question number
        holder.txtNumber.text = "Pregunta ${position + 1} de ${questions.size}"
        holder.txtText.text = question.text

        val selectedOptionIndex = selectedAnswers[question.id]
        val isAnswered = selectedOptionIndex != null
        val isMissing = missingQuestionIds.contains(question.id)

        // Reset default card styling
        holder.cardQuestion.strokeColor = ContextCompat.getColor(context, R.color.surface_card_stroke)
        holder.cardQuestion.strokeWidth = dpToPx(context, 1)

        if (isResultsRevealed) {
            val isCorrect = selectedOptionIndex == question.correctAnswerIndex
            if (isCorrect) {
                holder.txtStatus.text = "✓ Correcto"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_badge_answered)
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.answered_badge_text))
                holder.txtWarning.visibility = View.GONE
                holder.cardQuestion.strokeColor = ContextCompat.getColor(context, R.color.success)
                holder.cardQuestion.strokeWidth = dpToPx(context, 2)
            } else {
                holder.txtStatus.text = "✗ Incorrecto"
                holder.txtStatus.setBackgroundResource(R.drawable.bg_badge_pending)
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.unanswered_badge_text))
                holder.txtWarning.visibility = View.VISIBLE
                holder.txtWarning.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                holder.txtWarning.text = "💡 Respuesta correcta: ${question.options[question.correctAnswerIndex]}"
                holder.txtWarning.setBackgroundResource(R.drawable.bg_badge)
                holder.txtWarning.setTextColor(ContextCompat.getColor(context, R.color.badge_hint_text))
                holder.cardQuestion.strokeColor = ContextCompat.getColor(context, R.color.error)
                holder.cardQuestion.strokeWidth = dpToPx(context, 2)
            }
        } else {
            // Normal quiz mode
            if (isAnswered) {
                holder.txtStatus.text = context.getString(R.string.badge_answered)
                holder.txtStatus.setBackgroundResource(R.drawable.bg_badge_answered)
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.answered_badge_text))
            } else {
                holder.txtStatus.text = context.getString(R.string.badge_pending)
                holder.txtStatus.setBackgroundResource(R.drawable.bg_badge_pending)
                holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.unanswered_badge_text))
            }

            // Show card warning if validation failed for this question
            if (isMissing && !isAnswered) {
                holder.txtWarning.visibility = View.VISIBLE
                holder.txtWarning.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_warning, 0, 0, 0)
                holder.txtWarning.text = "  Debes responder esta pregunta antes de enviar."
                holder.txtWarning.setBackgroundResource(R.drawable.bg_badge_pending)
                holder.txtWarning.setTextColor(ContextCompat.getColor(context, R.color.unanswered_badge_text))
                holder.cardQuestion.strokeColor = ContextCompat.getColor(context, R.color.error)
                holder.cardQuestion.strokeWidth = dpToPx(context, 2)
            } else {
                holder.txtWarning.visibility = View.GONE
            }
        }

        // Setup RadioGroup options
        holder.radioGroup.setOnCheckedChangeListener(null)
        holder.radioGroup.removeAllViews()

        question.options.forEachIndexed { optionIndex, optionText ->
            val radioButton = RadioButton(context).apply {
                id = View.generateViewId()
                textSize = 15f
                setPadding(dpToPx(context, 12), dpToPx(context, 12), dpToPx(context, 12), dpToPx(context, 12))
                
                val params = RadioGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, dpToPx(context, 6), 0, dpToPx(context, 6))
                }
                layoutParams = params

                if (isResultsRevealed) {
                    isEnabled = false
                    isChecked = (selectedOptionIndex == optionIndex)

                    when {
                        optionIndex == question.correctAnswerIndex -> {
                            text = "$optionText  ✓"
                            setTextColor(ContextCompat.getColor(context, R.color.success))
                            setBackgroundResource(R.drawable.bg_option_correct)
                            buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.success))
                        }
                        optionIndex == selectedOptionIndex -> {
                            text = "$optionText  ✗"
                            setTextColor(ContextCompat.getColor(context, R.color.error))
                            setBackgroundResource(R.drawable.bg_option_incorrect)
                            buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.error))
                        }
                        else -> {
                            text = optionText
                            setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                            setBackgroundResource(R.drawable.bg_option_item)
                            alpha = 0.6f
                        }
                    }
                } else {
                    isEnabled = true
                    alpha = 1.0f
                    text = optionText
                    setTextColor(ContextCompat.getColor(context, R.color.text_primary))
                    setBackgroundResource(R.drawable.bg_option_item)
                    buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.primary))
                    isChecked = (selectedOptionIndex == optionIndex)
                }
            }

            holder.radioGroup.addView(radioButton)
        }

        // Handle option selection only if not in results mode
        if (!isResultsRevealed) {
            holder.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val checkedRadioButton = group.findViewById<RadioButton>(checkedId)
                if (checkedRadioButton != null && checkedRadioButton.isPressed) {
                    val clickedIndex = group.indexOfChild(checkedRadioButton)
                    if (clickedIndex != -1) {
                        selectedAnswers[question.id] = clickedIndex
                        missingQuestionIds.remove(question.id)

                        holder.txtStatus.text = context.getString(R.string.badge_answered)
                        holder.txtStatus.setBackgroundResource(R.drawable.bg_badge_answered)
                        holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.answered_badge_text))
                        holder.txtWarning.visibility = View.GONE
                        holder.cardQuestion.strokeColor = ContextCompat.getColor(context, R.color.surface_card_stroke)
                        holder.cardQuestion.strokeWidth = dpToPx(context, 1)

                        onAnswerSelectedListener(selectedAnswers.size)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = questions.size

    private fun dpToPx(context: android.content.Context, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }
}
