package com.gym4every1.database

import com.gym4every1.models.auth_models.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

suspend fun fetchActivityLevel(supabaseClient: SupabaseClient): Int? {
    // Fetching the user profile from the "profiles" table using the userId
    val profiles = supabaseClient.from("profiles")
        .select(columns = Columns.list("id, username, gender, height, weight, dateofbirth, activity_level, weight_goal"))
        .decodeList<Profile>() // Decode into Profile data class
    val currentUser = supabaseClient.auth.currentUserOrNull()
    val userId = currentUser?.id

    // Find the profile with the matching userId and return their activity_level
    return profiles.find { it.id == userId }?.activity_level
}