package com.gym4every1.models.social_models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FeedViewModel : ViewModel() {
    // State to store posts
    val posts = mutableStateListOf<Post>()

    // Fetch posts from the database or an API
    suspend fun fetchPosts(supabaseClient: SupabaseClient) {
        withContext(Dispatchers.IO) {
            try {
                val fetchedPosts = supabaseClient.from("posts")
                    .select(columns = Columns.Companion.list())
                    .decodeList<Post>()

                posts.clear()
                posts.addAll(fetchedPosts.sortedByDescending { it.created_at })
            } catch (e: Exception) {
                println("Error fetching posts: ${e.message}")
            }
        }
    }
}