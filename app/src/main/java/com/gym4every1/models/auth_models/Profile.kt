package com.gym4every1.models.auth_models

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: String,
    val username: String,
    val profile_picture_url: String? = null,
    val gender: String?,
    val height: Int?,
    val weight: Int?,
    val dateofbirth: String?,
    val activity_level: Int?,
    val weight_goal: Int?,
)