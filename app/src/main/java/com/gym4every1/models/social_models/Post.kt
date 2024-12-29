package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val user_id: String,
    val username: String,
    val content: String,
    val media_url: String?,
    val created_at: String
)