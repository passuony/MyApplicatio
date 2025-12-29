package com.example.myapplication.domain.repository

import com.example.myapplication.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    // حل التعارض: حذف كلمة suspend هنا لأن Flow يعمل بشكل تلقائي في الخلفية
    fun getFavoriteBooks(): Flow<List<Book>>

    suspend fun searchBooks(query: String): Result<List<Book>>
}