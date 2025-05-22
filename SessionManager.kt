package com.example.passwordmanager

object SessionManager {
    private var unlocked = false

    fun isUnlocked() = unlocked

    fun unlock() {
        unlocked = true
    }

    fun lock() {
        unlocked = false
    }
}