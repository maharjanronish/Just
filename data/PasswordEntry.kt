package com.example.passwordmanager.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val label: String,
    val encryptedPassword: String
)