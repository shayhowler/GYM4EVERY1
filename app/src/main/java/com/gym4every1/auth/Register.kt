package com.gym4every1.auth

import com.gym4every1.models.auth_models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

suspend fun registerUser(
    supabaseClient: SupabaseClient,
    userEmail: String,
    userPassword: String,
    fullName: String,
    securityQuestion: String,
    answer: String,
) {
    try {
        // Step 1: Register user with Supabase authentication
        val signUpResponse = supabaseClient.auth.signUpWith(Email) {
            email = userEmail
            password = userPassword
        }

        val userId = signUpResponse?.id ?: throw IllegalStateException("User ID is null")


        println("User successfully registered with ID: $userId")

        // Step 3: Create the User object
        val user = User(
            id = userId.toString(),
            email = userEmail,
            full_name = fullName,
            security_question = securityQuestion,
            answer = answer
        )

        // Step 4: Insert user details into the `users` table
        val insertResult = supabaseClient.postgrest["users"].insert(user)

        println("User details inserted successfully into custom table: $insertResult")
    } catch (e: Exception) {
        println("Error during user registration: ${e.message}")
        throw e
    }
}