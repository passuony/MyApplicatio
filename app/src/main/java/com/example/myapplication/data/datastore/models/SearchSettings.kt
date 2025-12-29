package com.example.myapplication.data.datastore.models

// يمثل إعدادات البحث لدينا (3 إعدادات كما هو مطلوب)
data class SearchSettings(
    val query: String = "kotlin", // مصطلح البحث الأساسي
    val minRating: Int = 0,      // الحد الأدنى للتقييم (0-5)
    val onlyFreeBooks: Boolean = false // فلتر للكتب المجانية فقط
)