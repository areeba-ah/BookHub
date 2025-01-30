package com.example.bookhub.Navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Main : Screen("main")
    object SavedBooks : Screen("saved")
    object BookDetails : Screen("bookDetails/{id}{bookTitle}/{bookAuthor}/{bookDescription}/{bookImageUrl}") {
        fun createRoute(id: String, bookTitle: String, bookAuthor: String, bookDescription: String, bookImageUrl: String, bookUrl: String): String {
            return "bookDetails/$id/$bookTitle/$bookAuthor/$bookDescription/$bookImageUrl/$bookUrl"
        }
    }
}