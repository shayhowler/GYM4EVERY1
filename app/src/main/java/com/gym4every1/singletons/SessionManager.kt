package com.gym4every1.singletons

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object SessionManager {

    private const val PREFS_NAME = "session_prefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_ENCRYPTED_ACCESS_TOKEN = "encryptedAccessToken"

    // Initialize Keystore
    private fun getKeystore(): KeyStore {
        return KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    }

    // Key Generator for securely transferring key
    private fun getSecretKey(): SecretKey {
        val keyStore = getKeystore()
        return if (!keyStore.containsAlias(KEY_ACCESS_TOKEN)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ACCESS_TOKEN,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()
            )
            keyGenerator.generateKey()
        } else {
            keyStore.getKey(KEY_ACCESS_TOKEN, null) as SecretKey
        }
    }

    // Encrypt token for preventing leaks
    private fun encryptAccessToken(token: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(token.toByteArray())
        val encryptedToken = Base64.encodeToString(iv + encrypted, Base64.DEFAULT)
        return encryptedToken
    }

    // Decrypt for reading on launch
    private fun decryptAccessToken(encryptedToken: String): String {
        val data = Base64.decode(encryptedToken, Base64.DEFAULT)
        val iv = data.copyOfRange(0, 12)
        val encryptedData = data.copyOfRange(12, data.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encryptedData))
    }

    // Save the session with encrypted access token
    fun saveSession(context: Context, accessToken: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            val encryptedToken = encryptAccessToken(accessToken)
            putString(KEY_ENCRYPTED_ACCESS_TOKEN, encryptedToken)
            apply()
            println("Session saved securely.")
        }
    }

    // Clear Session
    fun clearSession(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        println("Session cleared.")
    }

    // Retrieve Session Tokens
    fun getSession(context: Context): String? {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val encryptedAccessToken = sharedPreferences.getString(KEY_ENCRYPTED_ACCESS_TOKEN, null)
        return encryptedAccessToken?.let { decryptAccessToken(it) }
    }

    // Listen for Auth Changes
    suspend fun listenForAuthChanges(context: Context, supabaseClient: SupabaseClient) {
        supabaseClient.auth.sessionStatus.collect { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val session = supabaseClient.auth.currentSessionOrNull()
                    session?.let {
                        saveSession(context, it.accessToken)
                    }
                }
                is SessionStatus.NotAuthenticated -> {
                    if (status.isSignOut) {
                        println("User signed out.")
                        clearSession(context)
                    } else {
                        println("User not signed in.")
                    }
                }
                else -> {
                    println("Auth status: $status")
                }
            }
        }
    }

    // Check if Authenticated
    fun isAuthenticated(context: Context): Boolean {
        val session = getSession(context)
        return session != null
    }
}