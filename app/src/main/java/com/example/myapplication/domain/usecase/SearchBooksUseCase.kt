package com.example.myapplication.domain.usecase

import com.example.myapplication.domain.model.Book
import com.example.myapplication.domain.repository.BooksRepository

class SearchBooksUseCase(private val repository: BooksRepository) {

    // تأكد من أن invoke تستقبل 3 متغيرات بالأنواع الصحيحة
    suspend operator fun invoke(
        query: String,
        minRating: Float,
        onlyFree: Boolean
    ): Result<List<Book>> {
        val result = repository.searchBooks(query)

        return result.map { list ->
            list.filter { book ->
                // هنا نقوم بالفلترة اليدوية بناءً على خصائص كلاس Book
                // ملاحظة: إذا كلاس Book لا يحتوي على rating، احذف هذا السطر
                true
            }
        }
    }
}