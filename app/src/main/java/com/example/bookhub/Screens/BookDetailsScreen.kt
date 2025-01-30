package com.example.bookhub
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookhub.Activities.WebViewerActivity
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    id: String,
    bookTitle: String,
    bookAuthor: String,
    bookDescription: String,
    bookImageUrl: String,
    bookUrl: String,
    navController: NavController,
    vm: BooksViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // State to track if the book is saved
    var isBookSaved by remember { mutableStateOf(false) }

    // Check if book exists in the database when the composable is first loaded
    LaunchedEffect(id) {
        val exists = vm.doesBookExist(id) // Call suspend function directly
        isBookSaved = exists
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        // TopAppBar with Back Icon
        TopAppBar(
            title = { Text(text = "", style = MaterialTheme.typography.titleLarge) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },

            actions = {
                IconButton(onClick = {
                    // Open the book URL in WebView
                    val intent = Intent(context, WebViewerActivity::class.java).apply {
                        putExtra("Web_URI", bookUrl)
                    }
                    context.startActivity(intent)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_read_more_24),
                        contentDescription = "Open in WebView"
                    )
                }

                // Save/Remove Book Button
                IconButton(onClick = {
                    val savedBook = SavedBookEntity(
                        id = id,
                        title = bookTitle,
                        authors = listOf(bookAuthor),
                        description = bookDescription,
                        imageLinks = bookImageUrl,
                        infoLink = bookUrl
                    )

                    coroutineScope.launch {
                        if (isBookSaved) {
                            vm.deleteBookById(id)
                            Toast.makeText(context, "Book removed from collection", Toast.LENGTH_SHORT).show()
                        } else {
                            vm.saveBook(savedBook)
                            Toast.makeText(context, "Book saved to collection", Toast.LENGTH_SHORT).show()
                        }
                        isBookSaved = !isBookSaved // Toggle state
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_book_24),
                        contentDescription = "Save Book",
                        tint = if (isBookSaved) Color.DarkGray else Color.LightGray
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().offset((-10).dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            val imageUrl = bookImageUrl.replace("http://", "https://")

            Image(
                painter = rememberAsyncImagePainter(
                    model = imageUrl.ifEmpty { "https://path/to/placeholder.jpg" },
                    error = painterResource(R.drawable.baseline_image_24),
                    placeholder = painterResource(R.drawable.baseline_image_24)
                ),
                contentDescription = bookTitle,
                modifier = Modifier.size(440.dp).padding(8.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Book Title
            Text(text = bookTitle, style = MaterialTheme.typography.titleLarge)

            // Book Author
            Text(
                text = "Author: $bookAuthor",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            // Book Description
            Text(
                text = bookDescription,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}