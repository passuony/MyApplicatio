package com.example.myapplication.domain.model

data class UserProfile(
    val fullName: String = "",
    val avatarUri: String = "", // URI الصورة على الجهاز
    val resumeUrl: String = "", // رابط الـ PDF
    val position: String = "" ,  // حقل إضافي (المنصب)
    val favoriteLessonTime: String = ""
)