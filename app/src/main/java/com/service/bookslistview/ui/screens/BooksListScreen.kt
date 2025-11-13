package com.service.bookslistview.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.service.bookslistview.data.model.BookItem
import com.service.bookslistview.data.repository.ApiStates
import com.service.bookslistview.ui.viewmodel.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksListScreen(booksViewModel: BooksViewModel,
                    onBookClick: (BookItem) -> Unit) {
    val books by booksViewModel.booksState.collectAsState()
    var query by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        booksViewModel.fetchBooks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Books") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchBooks(query,
                onQueryChange = { query = it })

            Spacer(modifier = Modifier.padding(4.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when (books) {
                    is ApiStates.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }

                    is ApiStates.Error -> {
                        val errorMessage = (books as ApiStates.Error).message
                        Text(errorMessage, modifier = Modifier.align(Alignment.Center))
                    }

                    is ApiStates.Success -> {
                        val booksList = (books as ApiStates.Success).data
                        val filteredBooks = if (query.isEmpty()) booksList.items else {
                            booksList.items?.let { item ->

                                item.filter {
                                    it.volumeInfo.title?.contains(query, ignoreCase = true) == true
                                            || it.volumeInfo.authors?.get(0)
                                        ?.contains(query, ignoreCase = true) == true
                                }
                            }
                        }
                        BooksListItemView(filteredBooks,onBookClick)
                    }
                }
            }
        }

    }
}

@Composable
private fun BooksListItemView(filteredBooks: List<BookItem>?,
                              onBookClick: (BookItem) -> Unit) {
    LazyColumn {
        filteredBooks?.size?.let {
            items(it) { index ->
                val book = filteredBooks[index]
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(120.dp),
                    onClick = { onBookClick(book) }
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        BookImageFromUrl(book)
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            book.volumeInfo.title?.let { text ->
                                Text(
                                    text,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Spacer(modifier = Modifier.padding(4.dp))
                            book.volumeInfo.authors?.get(0)?.let { text ->
                                Text(
                                    text,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BookImageFromUrl(book: BookItem) {
    AsyncImage(
        model = book.volumeInfo.imageLinks?.thumbnail?.replace(
            "http://",
            "https://"
        ),
        contentDescription = "books",
        contentScale = ContentScale.Crop,
        modifier = Modifier.aspectRatio(3f / 4f)
    )
}

@Composable
private fun SearchBooks(query: String,
            onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search for books...") },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}