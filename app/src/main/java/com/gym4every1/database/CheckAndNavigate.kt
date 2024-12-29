package com.gym4every1.database

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.navigation.NavController
import com.gym4every1.AppActivity
import com.gym4every1.models.auth_models.Profile
import com.gym4every1.models.auth_models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

// Global helper function to check user and navigate accordingly
suspend fun checkAndNavigate(
    navController: NavController,
    supabaseClient: SupabaseClient,
    email: String,
    context: Context
) {
    val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id
    if (userId != null) {
        // Fetch existing users from the "users" table
        val existingUsers = supabaseClient.from("users").select(columns = Columns.list("id, email, username"))
            .decodeList<User>()

        val existingUser = existingUsers.firstOrNull { it.email == email }

        if (existingUser != null) {
            // Check if the user has a username set
            if (existingUser.username == null) {
                navController.navigate("signUp1") // Redirect to SignUp if no username
            } else {
                // Check if user has a profile in the "profiles" table
                val existingProfiles = supabaseClient.from("profiles")
                    .select(columns = Columns.list("id, username, profile_picture_url, gender, weight, height, dateofbirth, activity_level, weight_goal"))
                    .decodeList<Profile>()

                val userProfile = existingProfiles.firstOrNull { it.username == existingUser.username }

                // Navigate based on the user's profile data
                if (userProfile?.weight != null) {
                    val intent = Intent(context, AppActivity::class.java)
                    context.startActivity(intent)
                } else {
                    navController.navigate("getStarted") // If no weight, go to get started page
                }
            }
        } else {
            // Handle case where the user is not found in the "users" table
            val errorMessage = "User not found"
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            navController.navigate("authHome")
        }
    }
}