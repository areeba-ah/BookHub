package com.example.bookhub

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {@GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("startIndex") startIndex: Int
    ): BookResponse
}