package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class Vibe(
    val id: String,              // Unique ID for the vibe
    val user_id: String,         // ID of the user who created the vibe
    val username: String,        // Username of the vibe owner
    val content: String,         // Vibe content
    val media_url: String?,      // Media URL (optional)
    val parent_vibe_id: String?, // ID of the parent vibe (if it's a reply)
    val deleted: Boolean = false, // Indicates if the vibe is deleted
    var like_count: Int?,
    var child_count: Int?,
    val created_at: String,      // Creation timestamp
    val updated_at: String       // Last update timestamp
)