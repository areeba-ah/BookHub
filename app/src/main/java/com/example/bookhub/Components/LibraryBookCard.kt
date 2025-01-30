package com.example.bookhub
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bookhub.Navigation.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun LibraryBookCard(book: BookItem, navController: NavController) {
    BookCard(
        title = book.volumeInfo.title,
        authors = book.volumeInfo.authors ?: listOf("Unknown Author"),
        description = book.volumeInfo.description,
        imageUrl = book.volumeInfo.imageLinks?.thumbnail,
        onCardClick = {
            // URL encode and navigate to the BookDetails screen
            val encodedId = URLEncoder.encode(book.id, StandardCharsets.UTF_8.toString())
            val encodedTitle = URLEncoder.encode(book.volumeInfo.title, StandardCharsets.UTF_8.toString())
            val encodedAuthors = URLEncoder.encode(book.volumeInfo.authors?.joinToString(", ") ?: "Unknown", StandardCharsets.UTF_8.toString())
            val encodedDescription = URLEncoder.encode(book.volumeInfo.description ?: "No description", StandardCharsets.UTF_8.toString())
            val encodedImageUrl = URLEncoder.encode(book.volumeInfo.imageLinks?.thumbnail ?: "", StandardCharsets.UTF_8.toString())
            val encodedBookUrl = URLEncoder.encode(book.volumeInfo.infoLink.takeIf { it?.isNotEmpty() == true } ?: "", StandardCharsets.UTF_8.toString())

            val route = Screen.BookDetails.createRoute(encodedId, encodedTitle, encodedAuthors, encodedDescription, encodedImageUrl, encodedBookUrl)
            route?.let { navController.navigate(it) }
        }
    )
}
