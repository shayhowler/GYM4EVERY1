package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class PostLikes(
    val id: String,
    val user_id: String,
    val username: String,
    val post_id: String
)