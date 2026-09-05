package com.example.desafio2dsm.model

data class Question(
    val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)
