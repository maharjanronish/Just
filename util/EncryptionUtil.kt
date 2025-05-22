package com.example.passwordmanager.util

import android.content.Context
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.GCMParameterSpec

object EncryptionUtil {
    private const val PREF_NAME = "secure_prefs"
    private const val SALT_KEY = "salt"
    private const val ITERATION_COUNT = 10000
    private const val KEY_LENGTH = 256
    private const val AES_MODE = "AES/GCM/NoPadding"

    private var secretKey: SecretKey? = null
    private lateinit var prefs: EncryptedSharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

        prefs = EncryptedSharedPreferences.create(
            context, PREF_NAME, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        PasswordDatabase.init(context)
    }

    fun tryUnlock(password: String): Boolean {
        val salt = prefs.getString(SALT_KEY, null) ?: run {
            val newSalt = generateRandomBytes(16)
            prefs.edit().putString(SALT_KEY, Base64.encodeToString(newSalt, Base64.DEFAULT)).apply()
            Base64.encodeToString(newSalt, Base64.DEFAULT)
        }

        val saltBytes = Base64.decode(salt, Base64.DEFAULT)
        secretKey = deriveKey(password, saltBytes)
        return secretKey != null
    }

    fun encrypt(data: String): String? {
        return try {
            val cipher = Cipher.getInstance(AES_MODE)
            val iv = generateRandomBytes(12)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)
            val encrypted = cipher.doFinal(data.toByteArray())
            val combined = iv + encrypted
            Base64.encodeToString(combined, Base64.DEFAULT)
        } catch (e: Exception) {
            null
        }
    }

    fun decrypt(encrypted: String): String? {
        return try {
            val combined = Base64.decode(encrypted, Base64.DEFAULT)
            val iv = combined.sliceArray(0 until 12)
            val cipherText = combined.sliceArray(12 until combined.size)
            val cipher = Cipher.getInstance(AES_MODE)
            val spec = GCMParameterSpec(128, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
            String(cipher.doFinal(cipherText))
        } catch (e: Exception) {
            null
        }
    }

    private fun deriveKey(password: String, salt: ByteArray): SecretKey {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val secret = factory.generateSecret(spec).encoded
        return SecretKeySpec(secret, "AES")
    }

    private fun generateRandomBytes(length: Int): ByteArray {
        return ByteArray(length) { (0..255).random().toByte() }
    }
}