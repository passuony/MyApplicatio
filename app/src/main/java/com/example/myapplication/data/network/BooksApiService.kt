package com.example.myapplication.data.network

import com.example.myapplication.data.network.response.BooksApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {

    // طلب البحث عن الكتب
    @GET("volumes")
    suspend fun searchBooks(
        // @Query هو المتطلب المخصص (Custom Query Parameter)
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10
    ): BooksApiResponse

    // يمكنك إضافة طلب للحصول على تفاصيل كتاب واحد باستخدام Path Parameter لاحقاً
}