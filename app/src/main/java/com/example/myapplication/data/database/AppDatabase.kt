package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.database.dao.FavoriteBookDao // استيراد الـ DAO
import com.example.myapplication.data.database.entity.FavoriteBookEntity // استيراد الـ Entity

// [حل مشكلة MissingType]: التأكد من أن Entity مضافة هنا ومستوردة بشكل صحيح
@Database(entities = [FavoriteBookEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // [هام جداً]: يجب تعريف الدالة التي تعيد الـ DAO
    abstract fun favoriteBookDao(): FavoriteBookDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "book_database"
                )
                    .fallbackToDestructiveMigration() // يسمح بمسح البيانات عند تغيير الإصدار لتجنب الانهيار
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}