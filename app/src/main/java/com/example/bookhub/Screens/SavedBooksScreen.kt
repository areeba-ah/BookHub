package com.example.bookhub
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun SavedBooksScreen(vm: BooksViewModel, navController: NavController) {
    val savedBooks = vm.savedBooks.value

    Log.d("SavedBooksScreen", "Current saved book count: ${savedBooks.size}")

    LaunchedEffect(Unit) {
        vm.loadSavedBooks()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Empty state handling
        if (savedBooks.isEmpty()) {
            item {
                // Display message when the list is empty
                Box(
                    modifier = Modifier
                        .fillParentMaxSize() // Use fillParentMaxSize for LazyColumn
                        .background(Color.Black), // Ensure a contrasting background
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.noSavedBook).uppercase(),
                        color = Color.White,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        } else {
            // Iterate through the saved books
            items(savedBooks) { savedBook ->
                // Modify this line to pass the correct model (SavedBookEntity)
                SavedBookCard(savedBook, navController)
            }
        }
    }
}