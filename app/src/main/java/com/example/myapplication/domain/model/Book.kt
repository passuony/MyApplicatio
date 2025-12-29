package com.example.myapplication.domain.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val imageUrl: String,
    val year: String = "",
    val description: String = "",
    val genre: String = "",
    val pages: Int = 0
)