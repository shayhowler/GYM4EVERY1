package com.gym4every1.models.social_models
import kotlinx.serialization.Serializable

@Serializable
data class SavedVibe(
    val id: String,              // Unique ID for the vibe
    val user_id: String,         // ID of the user who created the vibe
    val vibe_id: String,
    val created_at: String,      // Creation timestamp
    val updated_at: String       // Last update timestamp
)
