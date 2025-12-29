package com.example.myapplication.domain.cache

import com.example.myapplication.data.datastore.models.SearchSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// كلاس بسيط لتخزين الحالة المشتركة لـ Badge
class SettingsCache {

    // الحالة الافتراضية للفلترة (بدون فلتر)
    private val DEFAULT_SETTINGS = SearchSettings(
        query = "kotlin",
        minRating = 0,
        onlyFreeBooks = false
    )

    // Flow يحمل حالة الـ Badge (هل هي مفعّلة أم لا)
    private val _isFiltered = MutableStateFlow(false)
    val isFiltered: StateFlow<Boolean> = _isFiltered

    // دالة لتحديث حالة الـ Badge
    fun updateFilterStatus(currentSettings: SearchSettings) {
        // التحقق مما إذا كانت الإعدادات الحالية مطابقة للإعدادات الافتراضية
        _isFiltered.value = currentSettings != DEFAULT_SETTINGS
    }
}