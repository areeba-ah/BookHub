package com.example.bookhub


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromListToString(authors: List<String>?): String {
        return Gson().toJson(authors)
    }

    @TypeConverter
    fun fromStringToList(authors: String?): List<String> {
        return if (authors.isNullOrEmpty()) {
            emptyList()
        } else {
            val type = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(authors, type)
        }
    }
}