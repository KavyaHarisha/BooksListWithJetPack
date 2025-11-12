package com.service.bookslistview.data.repository

import com.service.bookslistview.data.model.BooksResponse
import com.service.bookslistview.data.network.BooksApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BooksRepository @Inject constructor(private val booksApiService: BooksApiService) {

    /**
     * Fetches a list of books from the remote data source based on a search query.
     *
     * This function creates a cold Flow that, when collected, will:
     * 1. Emit a `ApiStates.Loading` state to indicate the operation has started.
     * 2. Attempt to fetch book data using `booksApiService.getBooks(query)`.
     * 3. If successful, it emits `ApiStates.Success` with the retrieved [BooksResponse].
     * 4. If an error occurs, it emits `ApiStates.Error` with a descriptive message.
     *
     * The entire flow is executed on the IO dispatcher, making it safe for network operations.
     *
     * @param query The search term to find books. Defaults to "android".
     * @return A [Flow] that emits the various states of the API request, encapsulated in an [ApiStates] object.
     */
     fun getAllBooks(query: String= "android"): Flow<ApiStates<BooksResponse>>{
        return flow {
            emit(ApiStates.Loading)
            try {
            val booksResponse = booksApiService.getBooks(query)
                    emit(ApiStates.Success(booksResponse))
                }catch (e: Exception){
                    emit(ApiStates.Error(e.localizedMessage ?: "Something went wrong"))
                }
        }.flowOn(Dispatchers.IO)
    }

}

sealed class ApiStates <out T> {
    object Loading : ApiStates<Nothing>()
    data class Success<T>(val data: T) : ApiStates<T>()
    data class Error(val message: String) : ApiStates<Nothing>()
}