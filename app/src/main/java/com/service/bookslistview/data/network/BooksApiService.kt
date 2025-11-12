package com.service.bookslistview.data.network

import com.service.bookslistview.data.model.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksApiService {

    @GET("volumes")
    suspend fun getBooks(@Query("q") query: String = "android"): BooksResponse

}