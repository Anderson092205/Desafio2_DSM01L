package com.example.desafio2dsm.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2dsm.R
import com.example.desafio2dsm.model.QuizCategory

class QuizCategoryAdapter(
    private val categories: List<QuizCategory>,
    private val onCategoryClick: (QuizCategory) -> Unit
) : RecyclerView.Adapter<QuizCategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgCategoryIcon)
        val txtTitle: TextView = itemView.findViewById(R.id.txtCategoryTitle)
        val txtDescription: TextView = itemView.findViewById(R.id.txtCategoryDescription)
        val txtCount: TextView = itemView.findViewById(R.id.txtCategoryQuestionCount)
        val cardCategory: View = itemView.findViewById(R.id.cardCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.txtTitle.text = category.title
        holder.txtDescription.text = category.description
        holder.txtCount.text = "${category.questions.size} Preguntas"

        val iconRes = when (category.iconName) {
            "ic_flag" -> R.drawable.ic_flag
            "ic_code" -> R.drawable.ic_code
            "ic_globe" -> R.drawable.ic_globe
            "ic_science" -> R.drawable.ic_science
            else -> R.drawable.ic_flag
        }
        holder.imgIcon.setImageResource(iconRes)

        holder.cardCategory.setOnClickListener {
            onCategoryClick(category)
        }
    }

    override fun getItemCount(): Int = categories.size
}
