package com.example.myapplication.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_books")
data class FavoriteBookEntity(
    @PrimaryKey val bookId: String,
    val title: String,
    val author: String,
    val imageUrl: String // يجب أن يكون الاسم مطابقاً تماماً لما تستدعيه في Repository
)