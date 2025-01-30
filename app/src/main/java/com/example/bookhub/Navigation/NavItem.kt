package com.example.bookhub.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.bookhub.R

sealed class NavItem(val title: String, val icon: @Composable () -> Painter) {
    object Library : NavItem(
        title = "Library",
        icon = { painterResource(id = R.drawable.baseline_library_books_24) } // No need for the .svg extension
    )

    object SavedBooks : NavItem(
        title = "Saved Books",
        icon = { painterResource(id = R.drawable.baseline_book_24) }
    )

    object AppInfo : NavItem(
        title = "App Info",
        icon = { painterResource(id = R.drawable.baseline_book_24) }
    )
}