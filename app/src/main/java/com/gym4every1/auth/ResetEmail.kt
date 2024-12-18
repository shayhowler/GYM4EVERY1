package com.gym4every1.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth

suspend fun sendPasswordResetEmail(
    supabaseClient: SupabaseClient,
    userEmail: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        supabaseClient.auth.resetPasswordForEmail(email = userEmail)
        onSuccess()
    } catch (e: Exception) {
        onError(e.message ?: "An unexpected error occurred")
    }
}