package com.example.bookhub
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SavedBookDao {
    @Insert
    suspend fun insertBook(book: SavedBookEntity)

    @Query("SELECT * FROM saved_books")
    suspend fun getAllBooks(): List<SavedBookEntity>


    @Query("DELETE FROM saved_books")
    suspend fun deleteAllBooks()

    @Query("DELETE FROM saved_books WHERE id = :bookId")
    suspend fun deleteBookById(bookId: String)

    @Query("SELECT * FROM saved_books WHERE id = :bookId LIMIT 1")
    suspend fun getBookById(bookId: String): SavedBookEntity?
}