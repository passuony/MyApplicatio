package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.BooksRepositoryImpl
import com.example.myapplication.di.AppContainer
import com.example.myapplication.domain.model.Book
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookDetailViewModel(
    private val bookId: String,
    private val repository: BooksRepositoryImpl,
    private val appContainer: AppContainer
) : ViewModel() {

    // [التحميل الفوري]: يأخذ الكتاب من الذاكرة (AppContainer) مباشرة
    private val _book = MutableStateFlow<Book?>(appContainer.selectedBook)
    val book = _book.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite = _isFavorite.asStateFlow()

    val favoriteBooks: StateFlow<List<Book>> = repository.getFavoriteBooks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        checkIfFavorite()
    }

    private fun checkIfFavorite() {
        viewModelScope.launch {
            _isFavorite.value = repository.isBookFavorite(bookId)
        }
    }

    fun toggleFavorite(book: Book) {
        viewModelScope.launch {
            repository.toggleFavorite(book)
            _isFavorite.value = repository.isBookFavorite(book.id)
        }
    }

    class Factory(
        private val bookId: String,
        private val repository: BooksRepositoryImpl,
        private val appContainer: AppContainer // تمرير الـ Container للمصنع
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BookDetailViewModel(bookId, repository, appContainer) as T
        }
    }
}