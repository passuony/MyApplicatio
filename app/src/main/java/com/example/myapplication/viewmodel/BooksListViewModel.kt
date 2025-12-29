package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datastore.SearchSettingsDataStore
import com.example.myapplication.domain.usecase.SearchBooksUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// تأكد أن هذا الكلاس غير مكرر في ملف آخر بنفس الاسم
// data class BookListState( ... )

class BooksListViewModel(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val settingsDataStore: SearchSettingsDataStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookListState())
    val uiState: StateFlow<BookListState> = _uiState.asStateFlow()

    init {
        // بحث افتراضي عند التشغيل
        search("Android Programming")
    }

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                // جلب آخر الإعدادات المحفوظة
                val settings = settingsDataStore.settingsFlow.first()

                // تنفيذ البحث مع تحويل minRating لـ Float
                val result = searchBooksUseCase(
                    query = query,
                    minRating = settings.minRating.toFloat(),
                    onlyFree = settings.onlyFreeBooks
                )

                result.onSuccess { books ->
                    _uiState.update { it.copy(isLoading = false, books = books) }
                }.onFailure { throwable ->
                    _uiState.update { it.copy(isLoading = false, error = throwable.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}

class BooksListViewModelFactory(
    private val searchBooksUseCase: SearchBooksUseCase,
    private val settingsDataStore: SearchSettingsDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BooksListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BooksListViewModel(searchBooksUseCase, settingsDataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}