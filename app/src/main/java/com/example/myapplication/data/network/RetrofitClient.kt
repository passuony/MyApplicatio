package com.example.myapplication.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    private val client: OkHttpClient
        get() {
            val logging = HttpLoggingInterceptor().apply {
                // ضروري لتصحيح الأخطاء وعرض الطلبات في Logcat أو App Inspector
                level = HttpLoggingInterceptor.Level.BODY
            }
            return OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
        }

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val apiService: BooksApiService by lazy {
        retrofit.create(BooksApiService::class.java)
    }
}