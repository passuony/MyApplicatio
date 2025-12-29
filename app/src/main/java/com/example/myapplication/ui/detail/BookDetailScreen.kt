package com.example.myapplication.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.myapplication.di.AppContainer
import com.example.myapplication.domain.model.Book
import com.example.myapplication.viewmodel.BookDetailViewModel
import com.example.myapplication.data.repository.BooksRepositoryImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(bookId: String, appContainer: AppContainer, onBack: () -> Unit) {
    // [تصحيح الخطأ]: تمرير appContainer إلى Factory
    val viewModel: BookDetailViewModel = viewModel(
        factory = BookDetailViewModel.Factory(
            bookId = bookId,
            repository = appContainer.booksRepository as BooksRepositoryImpl,
            appContainer = appContainer // تأكد من وجود هذا السطر هنا
        )
    )

    val book by viewModel.book.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val favoriteBooks by viewModel.favoriteBooks.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(book?.title ?: "Details", maxLines = 1, overflow = TextOverflow.Ellipsis) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { book?.let { viewModel.toggleFavorite(it) } }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            book?.let {
                BookDetailContent(it)
            } ?: Box(Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            if (favoriteBooks.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Books You Liked",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favoriteBooks) { favBook ->
                        FavoriteBookItem(favBook)
                    }
                }
            }
        }
    }
}

@Composable
fun BookDetailContent(book: Book) {
    Column(modifier = Modifier.padding(16.dp)) {
        AsyncImage(
            model = book.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = book.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(text = "By ${book.author}", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Description", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Text(text = book.description, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun FavoriteBookItem(book: Book) {
    Column(modifier = Modifier.width(100.dp)) {
        AsyncImage(
            model = book.imageUrl,
            contentDescription = null,
            modifier = Modifier.height(140.dp).fillMaxWidth().clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(text = book.title, maxLines = 1, fontSize = 12.sp, overflow = TextOverflow.Ellipsis)
    }
}