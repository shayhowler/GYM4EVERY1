package com.gym4every1.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

suspend fun sendPasswordResetEmail(
    supabaseClient: SupabaseClient,
    userEmail: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        // Step 1: Clean the email by removing any unwanted characters like double quotes
        val cleanedEmail = userEmail.replace("\"", "")

        // Step 2: Check if a user with the given email exists in the database
        val existingUsers = supabaseClient.postgrest["users"]
            .select(columns = Columns.list("email"))
            .decodeList<Map<String, String>>() // Decode as a list of email maps

        // Step 3: Look for the email in the fetched list
        val userExists = existingUsers.any { it["email"] == cleanedEmail }

        if (!userExists) {
            // If user doesn't exist, invoke the onError callback
            onError("No user found with the provided email address.")
            return
        }

        // Step 4: Send the password reset email
        supabaseClient.auth.resetPasswordForEmail(email = cleanedEmail)
        onSuccess()
    } catch (e: Exception) {
        onError(e.message ?: "An unexpected error occurred")
    }
}