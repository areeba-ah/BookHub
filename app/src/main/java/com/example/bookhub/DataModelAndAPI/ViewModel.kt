package com.example.bookhub
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.net.URL
import kotlin.math.pow

class BooksViewModel(private val database: AppDatabase): ViewModel() {
    private val _books = mutableStateOf<List<BookItem>>(emptyList())
    val books: State<List<BookItem>> = _books

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val _isLoadingMoreBooks = mutableStateOf(false)
    val isLoadingMoreBooks: State<Boolean> = _isLoadingMoreBooks

    private val _suggestions = mutableStateOf<List<String>>(emptyList())
    val suggestions: State<List<String>> = _suggestions

    private var currentIndex = 0
    private val maxResults = 40

    var lastSearchQuery by mutableStateOf("")

    val savedBooks = mutableStateOf<List<SavedBookEntity>>(emptyList())

    suspend fun doesBookExist(bookId: String): Boolean {
        return database.bookDao().getBookById(bookId) != null
    }

    fun saveBook(book: SavedBookEntity) {
        viewModelScope.launch {
            database.bookDao().insertBook(book)
        }
    }

    fun loadSavedBooks() {
        viewModelScope.launch {
            savedBooks.value = database.bookDao().getAllBooks()
        }
    }

    fun deleteBookById(bookId: String) {
        viewModelScope.launch {
            database.bookDao().deleteBookById(bookId)
        }
    }

    fun clearSavedBooks() {
        viewModelScope.launch {
            database.bookDao().deleteAllBooks()
        }
    }

    init {
        fetchBooks("programming books")  // Default query
    }


    private suspend fun fetchBooksFromApi(query: String, startIndex: Int): List<BookItem> {
        try {
            val response = RetrofitInstance.api.searchBooks(query, maxResults = maxResults, startIndex = startIndex)
            val books = response.items ?: emptyList()

            Log.d("BooksViewModel", "Fetched ${books.size} books from API for startIndex=$startIndex")
            return books
        } catch (e: Exception) {
            Log.e("BooksViewModel", "Error fetching books", e)
            return emptyList()
        }
    }

    fun fetchBooks(query: String, startIndex: Int = 0) {
        viewModelScope.launch {
            _loading.value = true
            Log.d("SearchBar", "Fetching books for query: $query")

            delay(600) // Debounce to avoid too many requests quickly
            val newBooks = fetchBooksFromApi(query, startIndex)

            if (startIndex == 0) {
                _books.value = newBooks // Replace list on first load
            } else {
                _books.value += newBooks // Append new books
            }

            if (newBooks.isNotEmpty()) {
                currentIndex = startIndex + maxResults
            } else {
                Log.d("BooksViewModel", "No more books to load for this query.")
            }

            _loading.value = false
        }
    }

    fun loadMoreBooks(query: String) {
        if (_isLoadingMoreBooks.value) {
            Log.d("BooksViewModel", "Already loading more books, skipping.")
            return
        }

        _isLoadingMoreBooks.value = true
        Log.d("BooksViewModel", "Loading more books for query: $query at index $currentIndex")

        viewModelScope.launch {
            delay(800) // Debounce load more to avoid rapid API calls

            val newBooks = fetchBooksFromApi(query, currentIndex)

            if (newBooks.isNotEmpty()) {
                _books.value += newBooks
                currentIndex += maxResults
            } else {
                Log.d("BooksViewModel", "No more books to load.")
            }

            _isLoadingMoreBooks.value = false
        }
    }

    // Fetch book suggestions
    fun fetchSuggestionsFromGoogleBooks(query: String) {
        if (query.isEmpty()) {
            _suggestions.value = emptyList() // Ensure suggestions are cleared for empty query
            return
        }

        viewModelScope.launch {
            try {
                val url = "https://www.googleapis.com/books/v1/volumes?q=$query"
                val response = withContext(Dispatchers.IO) { URL(url).readText() }

                val json = JSONObject(response)
                val items = json.optJSONArray("items") ?: JSONArray()
                val suggestionsList = mutableListOf<String>()

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val volumeInfo = item.getJSONObject("volumeInfo")
                    val title = volumeInfo.optString("title", "")
                    if (title.isNotEmpty()) {
                        suggestionsList.add(title)
                    }
                }

                _suggestions.value = suggestionsList
            } catch (e: Exception) {
                e.printStackTrace()
                _suggestions.value = emptyList()
            }
        }
    }

    // Clear suggestions
    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}