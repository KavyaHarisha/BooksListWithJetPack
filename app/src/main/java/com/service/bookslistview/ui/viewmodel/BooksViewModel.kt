package com.service.bookslistview.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.service.bookslistview.data.model.BooksResponse
import com.service.bookslistview.data.repository.ApiStates
import com.service.bookslistview.data.repository.BooksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val booksRepository: BooksRepository): ViewModel() {

    private var books: MutableStateFlow<ApiStates<BooksResponse>> = MutableStateFlow(ApiStates.Loading)
    val booksState: MutableStateFlow<ApiStates<BooksResponse>> = books

     fun fetchBooks(queryText: String = "android") {
        viewModelScope.launch {
            booksRepository.getAllBooks(queryText).collect {
                books.value = it
            }
        }
    }


}