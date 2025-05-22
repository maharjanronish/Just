package com.example.passwordmanager.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Query("SELECT * FROM PasswordEntry")
    fun getAll(): Flow<List<PasswordEntry>>

    @Insert
    suspend fun insert(entry: PasswordEntry)
}