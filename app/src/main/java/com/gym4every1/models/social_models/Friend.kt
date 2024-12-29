package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class Friend(
    val id : String,
    val user_id: String,
    val friend_id: String,
    val username: String,
    val friend_username: String,
    val status: String
)