package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.datastore.SearchSettingsDataStore
import com.example.myapplication.data.datastore.models.SearchSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val dataStore: SearchSettingsDataStore
) : ViewModel() {

    private val _settings = MutableStateFlow(SearchSettings())
    val settingsFlow = _settings.asStateFlow()

    init {
        // جلب الإعدادات المحفوظة عند بدء تشغيل ViewModel
        viewModelScope.launch {
            dataStore.settingsFlow.collect { savedSettings ->
                _settings.value = savedSettings
            }
        }
    }

    fun updateQuery(newQuery: String) {
        _settings.update { it.copy(query = newQuery) }
    }

    fun updateMinRating(newRating: Int) {
        _settings.update { it.copy(minRating = newRating) }
    }

    fun toggleFreeBooks(isChecked: Boolean) {
        _settings.update { it.copy(onlyFreeBooks = isChecked) }
    }

    fun saveSettings() {
        viewModelScope.launch {
            dataStore.saveSettings(_settings.value)
        }
    }
}

/**
 * [حل الخطأ]: إضافة الـ Factory لتمكين AppContainer من التعرف عليه
 */
class SettingsViewModelFactory(
    private val dataStore: SearchSettingsDataStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}