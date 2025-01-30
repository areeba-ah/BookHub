package com.example.bookhub

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun Library(viewModel: BooksViewModel, navController: NavController) {
    val books = viewModel.books.value
    val isLoading = viewModel.loading.value
    val isLoadingMoreBooks = viewModel.isLoadingMoreBooks.value

    // Search query state
    var searchText by rememberSaveable { mutableStateOf("") }

    // Collect suggestions from ViewModel (StateFlow)
    val suggestions = viewModel.suggestions.value

    // Remember LazyListState
    val listState = rememberLazyListState()

    // Clear the LazyListState when the search text changes
    LaunchedEffect(searchText) {
        listState.scrollToItem(0) // Reset the list to the top whenever searchText changes
    }

    // Handle text change, search, and suggestion click actions
    val onTextChange: (String) -> Unit = { text ->
        searchText = text
        if (text.isEmpty()) {
            viewModel.clearSuggestions() // Clear suggestions when text is empty
        } else {
            viewModel.fetchSuggestionsFromGoogleBooks(text) // Fetch suggestions as the user types
        }
    }

    val onSearch: (String) -> Unit = { query ->
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotEmpty()) {
            viewModel.fetchBooks(trimmedQuery) // Fetch books based on the new search term
            viewModel.clearSuggestions() // Clear suggestions after search
        }
    }

    val onSuggestionClick: (String) -> Unit = { suggestion ->
        searchText = suggestion // Update search text to selected suggestion
        viewModel.fetchBooks(suggestion) // Perform search with the suggestion
        viewModel.clearSuggestions() // Clear suggestions after selection
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search Bar UI
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Search Text Field
                BasicTextField(
                    value = searchText,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    singleLine = true,
                    cursorBrush = SolidColor(Color.White),
                    decorationBox = { innerTextField ->
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Book Title...",
                                style = TextStyle(color = Color.Gray, fontSize = 18.sp)
                            )
                        }
                        innerTextField()
                    }
                )

                // Search Icon
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            onSearch(searchText) // Fetch books when search icon is clicked
                            viewModel.clearSuggestions() // Clear suggestions after search
                        }
                )
            }
        }

        // Suggestions List
        if (suggestions.isNotEmpty() && searchText.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                items(suggestions) { suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSuggestionClick(suggestion) // Perform search with suggestion
                            }
                            .padding(8.dp),
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                }
            }
        }

        // Book List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(books, key = { index, book ->
                // Ensure that the key is unique by combining the book ID with the index
                "${book.id ?: book.volumeInfo}_${index}"
            }) { index, book ->
                LibraryBookCard(book, navController)
            }

            // Show progress indicator for initial loading or when loading more books.
            if (isLoading || isLoadingMoreBooks) {
                Log.d("Load", "Loading more: $isLoadingMoreBooks")
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

//        // Trigger loading more books when reaching the end of the list
        LaunchedEffect(listState) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo }
                .collect { visibleItems ->
                    val lastVisibleItem = visibleItems.lastOrNull()

                    if (lastVisibleItem != null) {
                        val isNearEnd = lastVisibleItem.index >= books.size - 5 // 5 items before the end

                        if (isNearEnd && !isLoadingMoreBooks) {
                            if (searchText.isNotEmpty()) {
                                viewModel.loadMoreBooks(searchText) // Only load more if search text is not empty
                            } else {
                                viewModel.loadMoreBooks("programming books") // Fallback default query
                            }
                        }
                    }
                }
        }


    }
}