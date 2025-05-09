package com.example.crmapp.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.crmapp.data.security.KeystoreManager

class JwtStorage(context: Context) {
    companion object {
        private const val PREF_NAME = "jwt_preferences"
        private const val KEY_TOKEN = "encrypted_token"
        private const val KEY_IV = "token_iv"
        private const val KEY_USER_ID = "user_id"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val keystoreManager = KeystoreManager()

    // Save the JWT token and user ID
    fun saveJwt(token: String, userId: String) {
        val encryptedData = keystoreManager.encryptToken(token)

        prefs.edit().apply {
            putString(KEY_TOKEN, encryptedData.encryptedData)
            putString(KEY_IV, encryptedData.iv)
            putString(KEY_USER_ID, userId)
            apply()
        }
    }

    // Get the JWT token
    fun getJwtToken(): String? {
        val encryptedToken = prefs.getString(KEY_TOKEN, null) ?: return null
        val iv = prefs.getString(KEY_IV, null) ?: return null

        return try {
            keystoreManager.decryptToken(
                KeystoreManager.EncryptedData(encryptedToken, iv)
            )
        } catch (e: Exception) {
            // If decryption fails (e.g., after app reinstall), clear stored data
            clearJwt()
            null
        }
    }

    // Get the user ID
    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    // Check if a JWT token exists
    fun hasJwt(): Boolean {
        return getJwtToken() != null && getUserId() != null
    }

    // Clear the stored JWT token
    fun clearJwt() {
        prefs.edit().clear().apply()
    }
}