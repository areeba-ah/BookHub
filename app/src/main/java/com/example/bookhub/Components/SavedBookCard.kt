package com.example.bookhub

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.bookhub.Navigation.Screen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun SavedBookCard(savedBook: SavedBookEntity, navController: NavController) {
    BookCard(
        title = savedBook.title,
        authors = savedBook.authors,
        description = savedBook.description,
        imageUrl = savedBook.imageLinks,
        onCardClick = {
            // URL encode and navigate to the BookDetails screen
            val encodedId = URLEncoder.encode(savedBook.id, StandardCharsets.UTF_8.toString())
            val encodedTitle = URLEncoder.encode(savedBook.title, StandardCharsets.UTF_8.toString())
            val encodedAuthors = URLEncoder.encode(savedBook.authors.joinToString(", "), StandardCharsets.UTF_8.toString())
            val encodedDescription = URLEncoder.encode(savedBook.description ?: "No description", StandardCharsets.UTF_8.toString())
            val encodedImageUrl = URLEncoder.encode(savedBook.imageLinks ?: "", StandardCharsets.UTF_8.toString())
            val encodedBookUrl = URLEncoder.encode(savedBook.infoLink ?: "", StandardCharsets.UTF_8.toString())

            val route = Screen.BookDetails.createRoute(encodedId, encodedTitle, encodedAuthors, encodedDescription, encodedImageUrl, encodedBookUrl)
            route?.let { navController.navigate(it) }
        }
    )
}