package com.gym4every1.database

import com.gym4every1.models.auth_models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

suspend fun fetchUserProfile(supabaseClient: SupabaseClient, userId: String): String? {
    // Fetching the user profile for the specified userId from the "users" table
    val existingUsers = supabaseClient.from("users")
        .select(columns = Columns.list("id, email, username"))
        .decodeList<User>()

    // Find the user with the matching ID and return their username
    return existingUsers.find { it.id == userId }?.username
}
