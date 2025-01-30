package com.example.bookhub

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_books")
data class SavedBookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authors: List<String>, // List<String> is now supported because of the TypeConverter
    val description: String?,
    val imageLinks: String?,
    val infoLink: String
)