package com.gym4every1.singletons

import android.content.Context
import android.content.SharedPreferences
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus

object SessionManager {

    private const val PREFS_NAME = "session_prefs"
    private const val KEY_ACCESS_TOKEN = "accessToken"
    private const val KEY_REFRESH_TOKEN = "refreshToken"

    fun saveSession(context: Context, accessToken: String, refreshToken: String) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            apply()
        }
        println("Session saved.")
    }

    fun clearSession(context: Context) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        println("Session cleared.")
    }

    fun getSession(context: Context): Pair<String?, String?> {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        val refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
        return Pair(accessToken, refreshToken)
    }

    suspend fun listenForAuthChanges(context: Context, supabaseClient: SupabaseClient) {
        supabaseClient.auth.sessionStatus.collect { status ->
            when (status) {
                is SessionStatus.Authenticated -> {
                    val session = supabaseClient.auth.currentSessionOrNull()
                    session?.let {
                        saveSession(context, it.accessToken, it.refreshToken)
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

    // Check if the user is authenticated
    fun isAuthenticated(context: Context): Boolean {
        val session = getSession(context)
        return session.first != null && session.second != null // Access token and refresh token must not be null
    }
}