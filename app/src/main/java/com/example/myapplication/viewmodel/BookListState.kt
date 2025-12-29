package com.example.myapplication.viewmodel


import com.example.myapplication.domain.model.Book

data class BookListState(
    val isLoading: Boolean = false,
    val books: List<Book> = emptyList(),
    val error: String? = null
)