package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class Comment (
    val id: String,
    val post_id: String,
    val user_id: String,
    val username: String,
    val content: String
)