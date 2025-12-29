package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.datastore.ProfileDataStore
import com.example.myapplication.data.datastore.SearchSettingsDataStore
import com.example.myapplication.data.repository.BooksRepositoryImpl
import com.example.myapplication.domain.model.Book
import com.example.myapplication.domain.usecase.SearchBooksUseCase
import com.example.myapplication.viewmodel.BooksListViewModelFactory
import com.example.myapplication.viewmodel.SettingsViewModelFactory

class AppContainer(private val context: Context) {

    private val database: AppDatabase by lazy { AppDatabase.getDatabase(context) }
    val searchSettingsDataStore = SearchSettingsDataStore(context)

    // [حل الخطأ]: إضافة ProfileDataStore هنا
    val profileDataStore by lazy { ProfileDataStore(context) }

    // للحفظ المؤقت للتحميل الفوري
    var selectedBook: Book? = null

    val booksRepository by lazy {
        BooksRepositoryImpl(database.favoriteBookDao())
    }

    private val searchBooksUseCase by lazy { SearchBooksUseCase(booksRepository) }

    fun getBooksListViewModelFactory(): BooksListViewModelFactory {
        return BooksListViewModelFactory(searchBooksUseCase, searchSettingsDataStore)
    }

    fun getSettingsViewModelFactory(): SettingsViewModelFactory {
        return SettingsViewModelFactory(searchSettingsDataStore)
    }
}