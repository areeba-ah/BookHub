package com.example.bookhub
import android.app.Application

class MyApplication : Application() {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(this)
    }
}

