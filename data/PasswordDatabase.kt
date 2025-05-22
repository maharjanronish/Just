package com.example.passwordmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PasswordEntry::class], version = 1)
abstract class PasswordDatabase : RoomDatabase() {
    abstract fun passwordDao(): PasswordDao

    companion object {
        @Volatile private var INSTANCE: PasswordDatabase? = null

        fun getInstance(): PasswordDatabase {
            return INSTANCE ?: throw IllegalStateException("DB not initialized")
        }

        fun init(context: Context) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PasswordDatabase::class.java,
                "passwords.db"
            ).build()
        }
    }
}