package com.gym4every1.models.auth_models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val email: String,
    val full_name: String,
    val security_question: String,
    val answer: String
)