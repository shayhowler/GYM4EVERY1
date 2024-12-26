package com.gym4every1.auth

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

suspend fun sendPasswordResetEmail(
    supabaseClient: SupabaseClient,
    userEmail: String,
    securityAnswer: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    try {
        // Step 1: Clean the email by removing any unwanted characters like double quotes
        val cleanedEmail = userEmail.replace("\"", "")

        // Step 2: Check if a user with the given email exists and fetch security question data
        val userRecords = supabaseClient.postgrest["users"]
            .select(columns = Columns.list("email", "answer"))
            .decodeList<Map<String, String>>() // Decode as a list of maps

        // Step 3: Find the user by email
        val userRecord = userRecords.find { it["email"] == cleanedEmail }

        if (userRecord == null) {
            onError("No user found with the provided email address.")
            return
        }

        // Step 4: Validate the security question's answer
        val correctAnswer = userRecord["answer"]
        if (correctAnswer == null || correctAnswer != securityAnswer) {
            onError("Incorrect answer to the security question.")
            return
        }

        // Step 5: Send the password reset email
        supabaseClient.auth.resetPasswordForEmail(email = cleanedEmail)
        onSuccess()
    } catch (e: Exception) {
        onError(e.message ?: "An unexpected error occurred")
    }
}