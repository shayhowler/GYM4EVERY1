package com.gym4every1.auth

import com.gym4every1.models.auth_models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

suspend fun registerUser(
    supabaseClient: SupabaseClient,
    userEmail: String,
    userPassword: String,
    fullName: String,
    username: String,
    securityQuestion: String,
    answer: String
): Boolean { // Returning boolean to track if the user was updated
    try {
        // Step 1: Clean up the email to remove any extra characters like double quotes
        val cleanedEmail = userEmail.replace("\"", "")

        // Step 2: Check if the user exists in the Supabase database
        val existingUsers = supabaseClient.postgrest["users"]
            .select(columns = Columns.list("id", "email"))
            .decodeList<User>()

        // Find if there is an existing user with the same email
        val existingUser = existingUsers.firstOrNull { it.email == cleanedEmail }

        // Step 3: Register the user if not already registered
        val userId = existingUser?.id ?: run {
            // If user doesn't exist, create the user in Supabase
            val signUpResponse = supabaseClient.auth.signUpWith(Email) {
                email = cleanedEmail
                password = userPassword
            }

            val newUserId = signUpResponse?.id ?: throw IllegalStateException("User ID is null")
            newUserId.toString() // Return the new user ID
        }

        // Step 4: Update the password if the user exists
        val isUserUpdated = if (existingUser != null) {
            val updatedUser = supabaseClient.auth.updateUser {
                password = userPassword
            }
            println("Password updated successfully: $updatedUser")
            true // Indicating the user was updated
        } else {
            false // User is new, so no update is needed
        }

        // Step 5: Upsert user details into the `users` table (always upsert)
        val user = User(
            id = userId,
            email = cleanedEmail,
            fullname = fullName,
            username = username,
            securityquestion = securityQuestion,
            answer = answer
        )

        val insertResult = supabaseClient.postgrest["users"].upsert(user)
        println("User details inserted/updated successfully into custom table: $insertResult")

        return isUserUpdated // Return boolean to indicate if the user was updated
    } catch (e: Exception) {
        println("Error during user registration: ${e.message}")
        throw e
    }
}