package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class VibeLikes(
    val id: String,       // Unique ID for the like
    val user_id: String,  // ID of the user who liked the vibe
    val username: String, // Username of the liker
    val vibe_id: String,  // ID of the liked vibe
    val created_at: String // Timestamp of when the vibe was liked
)