package com.example.desafio2dsm.model

data class QuizCategory(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val questions: List<Question>
)
