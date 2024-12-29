package com.gym4every1.database

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

suspend fun fetchOwnerProfilePicture(supabaseClient: SupabaseClient, userId: String): String? {
    // Fetching the user profile from the "profiles" table using the userId
    val profiles = supabaseClient.from("profiles")
        .select(columns = Columns.list("id, profile_picture_url"))
        .decodeList<Map<String, String>>()
    // Find the profile with the matching userId and return their profile picture URL
    return profiles.find { it["id"] == userId }?.get("profile_picture_url")
}