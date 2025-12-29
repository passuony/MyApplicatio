package com.example.myapplication.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.data.datastore.models.SearchSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// اسم ملف DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "search_settings")

class SearchSettingsDataStore(private val context: Context) {

    // تعريف مفاتيح التفضيلات
    private object PreferencesKeys {
        val SEARCH_QUERY = stringPreferencesKey("search_query")
        val MIN_RATING = intPreferencesKey("min_rating")
        val ONLY_FREE_BOOKS = booleanPreferencesKey("only_free_books")
    }

    // جلب الإعدادات (كـ Flow)
    val settingsFlow: Flow<SearchSettings> = context.dataStore.data
        .map { preferences ->
            SearchSettings(
                query = preferences[PreferencesKeys.SEARCH_QUERY] ?: "kotlin",
                minRating = preferences[PreferencesKeys.MIN_RATING] ?: 0,
                onlyFreeBooks = preferences[PreferencesKeys.ONLY_FREE_BOOKS] ?: false
            )
        }

    // حفظ الإعدادات
    suspend fun saveSettings(settings: SearchSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SEARCH_QUERY] = settings.query
            preferences[PreferencesKeys.MIN_RATING] = settings.minRating
            preferences[PreferencesKeys.ONLY_FREE_BOOKS] = settings.onlyFreeBooks
        }
    }
}