package com.example.myapplication.data.database.dao

import androidx.room.*
import com.example.myapplication.data.database.entity.FavoriteBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteBookDao {
    @Query("SELECT * FROM favorite_books")
    fun getAllFavorites(): Flow<List<FavoriteBookEntity>>

    @Query("SELECT EXISTS(SELECT * FROM favorite_books WHERE bookId = :bookId)")
    suspend fun isFavorite(bookId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: FavoriteBookEntity)

    @Delete
    suspend fun delete(book: FavoriteBookEntity)

    @Query("DELETE FROM favorite_books WHERE bookId = :bookId")
    suspend fun deleteById(bookId: String)
}