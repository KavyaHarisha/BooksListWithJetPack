package com.service.bookslistview.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.service.bookslistview.data.model.BookItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(bookData: BookItem, onBackClick: () -> Unit) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Book Details") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            })
    }) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                bookData.volumeInfo.imageLinks?.let { imageLink ->
                    val secureThumbnailUrl = imageLink.thumbnail?.replace("http://", "https://")

                    println("kavya Image Link: $secureThumbnailUrl")
                    Image(
                        painter = rememberAsyncImagePainter(secureThumbnailUrl),
                        contentDescription = bookData.volumeInfo.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    bookData.volumeInfo.title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    bookData.volumeInfo.authors?.let {
                        Text(text = it.joinToString(), style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(12.dp))
                    println("kavya desc: ${bookData.volumeInfo.description}")
                    Text(
                        text = bookData.volumeInfo.description ?: "No description available",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }


}