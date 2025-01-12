package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val id: String,            // Unique ID for the friend record
    val user_id: String,       // ID of the user
    val friend_id: String,     // ID of the friend
    val username: String,      // Username of the user
    val friend_username: String, // Username of the friend
    val status: String,        // Friendship status (e.g., pending, accepted, blocked)
    val created_at: String,    // Timestamp of creation
    val updated_at: String     // Timestamp of the last update
)