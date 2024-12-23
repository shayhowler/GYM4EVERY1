package com.gym4every1.models.auth_models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val fullname: String? = null,
    val username: String? = null,
    val securityquestion: String? = null,
    val answer: String? = null,
)