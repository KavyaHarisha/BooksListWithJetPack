package com.service.bookslistview

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.service.bookslistview.ui.screens.BooksListScreen
import com.service.bookslistview.ui.viewmodel.BooksViewModel

@Composable
fun BooksNavGraph(){
    val navController = rememberNavController()
    val booksViewModel: BooksViewModel = hiltViewModel()
    NavHost(navController, startDestination = "books"){
        composable("books"){
            BooksListScreen(booksViewModel)
        }
    }

}