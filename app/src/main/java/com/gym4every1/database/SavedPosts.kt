package com.gym4every1.database

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

suspend fun fetchSavedPosts(supabaseClient: SupabaseClient, userId: String): List<Map<String, Any>> {
    // Fetch all saved posts along with the post details
    return supabaseClient.from("saved_posts")
        .select(columns = Columns.list("user_id, post_id, posts:posts(content, media_url)"))
        .decodeList<Map<String, Any>>()
        .filter { it["user_id"] == userId }
}