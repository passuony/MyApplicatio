package com.example.myapplication.ui.favorites

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.di.AppContainer
import com.example.myapplication.domain.model.Book
import com.example.myapplication.ui.list.BookItemCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(appContainer: AppContainer, onBookClick: (Book) -> Unit) {
    // جلب الكتب المفضلة مباشرة من المستودع كـ Flow
    val favorites by appContainer.booksRepository.getFavoriteBooks().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favorites Books", fontWeight = FontWeight.Bold) }
            )
        }
    ) { padding ->
        if (favorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("There are no books in the favorites at the moment")
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = favorites, key = { it.id }) { book ->
                    // نستخدم نفس تصميم الكارد الموجود في شاشة القائمة لتوحيد الشكل
                    BookItemCard(
                        book = book,
                        onClick = { onBookClick(book) }
                    )
                }
            }
        }
    }
}