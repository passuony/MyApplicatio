package com.example.myapplication.data.repository

import com.example.myapplication.data.database.dao.FavoriteBookDao
import com.example.myapplication.data.database.entity.FavoriteBookEntity
import com.example.myapplication.domain.model.Book
import com.example.myapplication.domain.repository.BooksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// --- موديلات البيانات ---
data class GoogleBooksResponse(val items: List<BookItem>?)
data class BookItem(val id: String, val volumeInfo: VolumeInfo)
data class VolumeInfo(
    val title: String,
    val authors: List<String>?,
    val imageLinks: ImageLinks?,
    val description: String?
)
data class ImageLinks(val thumbnail: String)

interface BooksApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): GoogleBooksResponse
}

class BooksRepositoryImpl(
    private val favoriteBookDao: FavoriteBookDao
) : BooksRepository {

    private val apiService: BooksApiService = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/books/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BooksApiService::class.java)

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return favoriteBookDao.getAllFavorites().map { entities ->
            entities.map { entity ->
                Book(
                    id = entity.bookId,
                    title = entity.title,
                    author = entity.author,
                    imageUrl = entity.imageUrl,
                    year = "",
                    description = "",
                    genre = "",
                    pages = 0
                )
            }
        }
    }

    override suspend fun searchBooks(query: String): Result<List<Book>> {
        return try {
            val response = apiService.searchBooks(query)
            val books = response.items?.map { item ->
                Book(
                    id = item.id,
                    title = item.volumeInfo.title,
                    author = item.volumeInfo.authors?.joinToString(", ") ?: "Unknown Author",
                    imageUrl = item.volumeInfo.imageLinks?.thumbnail?.replace("http:", "https:") ?: "",
                    description = item.volumeInfo.description ?: ""
                )
            } ?: emptyList()
            Result.success(books)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(book: Book) {
        val isFav = favoriteBookDao.isFavorite(book.id)
        if (isFav) {
            favoriteBookDao.deleteById(book.id)
        } else {
            favoriteBookDao.insert(
                FavoriteBookEntity(
                    bookId = book.id,
                    title = book.title,
                    author = book.author,
                    imageUrl = book.imageUrl
                )
            )
        }
    }

    suspend fun isBookFavorite(bookId: String): Boolean = favoriteBookDao.isFavorite(bookId)
}