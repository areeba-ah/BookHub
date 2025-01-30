package com.example.bookhub
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [SavedBookEntity::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class) // Use the correct Converters class
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): SavedBookDao

    companion object {
        // Singleton pattern for creating the database instance
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "book_hub_database"
                )
                    .addMigrations(MIGRATION_1_2) // Handle migration from version 1 to 2
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // Define migration from version 1 to 2 if schema changes are made
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Add migration logic if required (e.g., alter table, add columns, etc.)
                // For now, no migration steps are needed if just adding converters
            }
        }
    }
}