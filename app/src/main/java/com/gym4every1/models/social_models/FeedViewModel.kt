package com.gym4every1.models.social_models

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gym4every1.models.auth_models.User
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.channel
import io.github.jan.supabase.realtime.postgresListDataFlow
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

// Adjust this to your current ViewModel
class FeedViewModel() : ViewModel() {
    val vibes = mutableStateListOf<Vibe>()
    val savedVibeIds = mutableStateListOf<String>() // Track saved vibe IDs
    private val supabaseClient = SupabaseClientManager.getSupabaseClient()
    val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id

    suspend fun getUserName(userId: String): String? {
        val response = supabaseClient.from("users")
            .select(columns = Columns.list("id, email, username")){
                filter {
                    eq("id", userId)
                }
            }
            .decodeSingle<User>()
        return response.username
    }

    private val vibeChannel = supabaseClient.channel("vibes_channel")
    private val likeChannel = supabaseClient.channel("vibe_likes_channel") // Added: channel for likes

    suspend fun fetchVibes(supabaseClient: SupabaseClient, showSaved: Boolean = false) {
        withContext(Dispatchers.IO) {
            try {
                if (userId == null) {
                    println("User is not authenticated")
                    return@withContext
                }

                // Fetch saved vibe IDs
                savedVibeIds.clear()
                val savedVibes = supabaseClient.from("saved_vibes")
                    .select()
                    .decodeList<SavedVibe>()
                    .filter { it.user_id == userId }

                savedVibeIds.addAll(savedVibes.map { it.vibe_id })

                // Fetch vibes
                val fetchedVibes = if (showSaved) {
                    vibes.filter { it.id in savedVibeIds }
                } else {
                    supabaseClient.from("vibes").select().decodeList<Vibe>()
                }

                vibes.clear()
                vibes.addAll(fetchedVibes.sortedByDescending { it.created_at })

                // Fetch like counts for each vibe
                fetchedVibes.forEach { vibe ->
                    vibe.like_count = supabaseClient.from("vibe_likes")
                        .select(Columns.list("id", "user_id", "username", "vibe_id", "created_at"))
                        .decodeList<VibeLikes>()
                        .count { it.vibe_id == vibe.id }
                }
            } catch (e: Exception) {
                println("Error fetching vibes: ${e.message}")
            }
        }
    }

    fun isVibeSaved(vibeId: String): Boolean {
        return savedVibeIds.contains(vibeId)
    }

    suspend fun saveVibe(supabaseClient: SupabaseClient, vibeId: String) {
        withContext(Dispatchers.IO) {
            try {
                // Insert a record into saved_vibes table
                supabaseClient.from("saved_vibes").insert(mapOf(
                    "user_id" to userId.toString(),
                    "vibe_id" to vibeId
                ))
            } catch (e: Exception) {
                println("Error saving vibe: ${e.message}")
            }
        }
    }

    suspend fun unsaveVibe(supabaseClient: SupabaseClient, vibeId: String) {
        withContext(Dispatchers.IO) {
            try {
                // Delete the record from saved_vibes table
                supabaseClient.from("saved_vibes").delete {
                    filter { eq("user_id", userId.toString()) }
                    filter { eq("vibe_id", vibeId) }
                }
            } catch (e: Exception) {
                println("Error unsaving vibe: ${e.message}")
            }
        }
    }

    suspend fun subscribeToVibesAndLikes() {
        // Keep your existing subscription to vibes
        val vibesFlow: Flow<List<Vibe>> = vibeChannel.postgresListDataFlow(
            schema = "public",
            table = "vibes",
            primaryKey = Vibe::id
        )

        viewModelScope.launch {
            vibesFlow.onEach { updatedVibes ->
                vibes.clear()
                vibes.addAll(updatedVibes.sortedByDescending { it.created_at })
            }.collect()
        }

        val vibeChangeFlow = vibeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "vibes"
        }

        viewModelScope.launch {
            vibeChangeFlow.onEach {
                when (it) {
                    is PostgresAction.Delete -> println("Deleted: ${it.oldRecord}")
                    is PostgresAction.Insert -> println("Inserted: ${it.record}")
                    is PostgresAction.Select -> println("Selected: ${it.record}")
                    is PostgresAction.Update -> println("Updated: ${it.oldRecord} with ${it.record}")
                }
            }.launchIn(this)
        }

        vibeChannel.subscribe()

        // Add subscription to likes
        val likesFlow = likeChannel.postgresChangeFlow<PostgresAction>(schema = "public") {
            table = "vibe_likes"
        }

        viewModelScope.launch {
            likesFlow.onEach { action ->
                when (action) {
                    is PostgresAction.Insert -> handleLikeAdded(action.record)
                    is PostgresAction.Delete -> handleLikeRemoved(action.oldRecord)
                    else -> {} // Handle other actions if necessary
                }
            }.launchIn(this)
        }

        likeChannel.subscribe()
    }

    private suspend fun handleLikeAdded(likeRecord: JsonObject) {
        // Deserialize the likeRecord to VibeLikes object
        val like = Json.decodeFromString<VibeLikes>(likeRecord.toString())

        // Find the corresponding vibe object
        val vibe = vibes.find { it.id == like.vibe_id }

        if (vibe != null) {
            // Increment the local like count
            vibe.like_count = (vibe.like_count ?: 0) + 1

            // Update the like count in the database
            updateLikeCountInDatabase(like.vibe_id, vibe.like_count)
        }
    }
    private fun handleLikeRemoved(likeRecord: JsonObject) {
        println("Like removed: $likeRecord")  // Debugging line
    }

    suspend fun updateLikeCountInDatabase(vibeId: String, newLikeCount: Int?) {
        try {
            // Update the like count in the vibes table in the database
            supabaseClient.from("vibes").update(mapOf(
                "like_count" to newLikeCount
            )){
                    filter { eq("id", vibeId) }
                }
        } catch (e: Exception) {
            println("Error while updating like count: ${e.message}")
        }
    }

    suspend fun likeVibe(supabaseClient: SupabaseClient, vibeId: String) {
        withContext(Dispatchers.IO) {
            try {
                val userName = getUserName(userId.toString())
                supabaseClient.from("vibe_likes").insert(mapOf(
                    "vibe_id" to vibeId,
                    "username" to userName,
                    "user_id" to userId.toString(),
                ))
            } catch (e: Exception) {
                println("Error liking vibe: ${e.message}")
            }
        }
    }

    suspend fun unlikeVibe(supabaseClient: SupabaseClient, vibeId: String) {
        withContext(Dispatchers.IO) {
            try {
                val userName = getUserName(userId.toString())

                // Find the corresponding vibe object from local data
                val vibe = vibes.find { it.id == vibeId }

                // If the vibe is found, update the like count before deletion
                if (vibe != null) {
                    // Decrement the local like count
                    vibe.like_count = (vibe.like_count ?: 0) - 1

                    // Update the like count in the database first
                    updateLikeCountInDatabase(vibeId, vibe.like_count)
                }

                // Now, delete the like record from the database
                supabaseClient.from("vibe_likes").delete {
                    filter { eq("vibe_id", vibeId) }
                    filter { eq("username", userName.toString()) }
                    filter { eq("user_id", userId.toString()) }
                }

            } catch (e: Exception) {
                println("Error unliking vibe: ${e.message}")
            }
        }
    }

    suspend fun unsubscribeFromVibesAndLikes() {
        vibeChannel.unsubscribe()
        likeChannel.unsubscribe()
    }

    override fun onCleared() {
        viewModelScope.launch {
            unsubscribeFromVibesAndLikes()
        }
        super.onCleared()
    }
}