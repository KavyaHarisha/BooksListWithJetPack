package com.service.bookslistview

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.service.bookslistview.data.model.BookItem
import com.service.bookslistview.ui.screens.BookDetailScreen
import com.service.bookslistview.ui.screens.BooksListScreen
import com.service.bookslistview.ui.viewmodel.BooksViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun BooksNavGraph(){
    val navController = rememberNavController()
    val booksViewModel: BooksViewModel = hiltViewModel()
    NavHost(navController, startDestination = "books"){
        composable("books"){
            BooksListScreen(booksViewModel) { book ->
                val json = URLEncoder.encode(Gson().toJson(book), StandardCharsets.UTF_8.toString())
                navController.navigate("details/$json")
            }
        }


        composable("details/{bookItem}",
            arguments = listOf(navArgument("bookItem") {
                type = NavType.StringType
            })){ backStackEntry ->
            val bookJson = backStackEntry.arguments?.getString("bookItem")?.let {
                URLDecoder.decode(it, StandardCharsets.UTF_8.toString())
            }
            val book = Gson().fromJson(bookJson, BookItem::class.java)
            BookDetailScreen(book){
                navController.popBackStack()
            }
        }
    }

}