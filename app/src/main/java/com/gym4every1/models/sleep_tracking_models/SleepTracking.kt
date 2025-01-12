package com.gym4every1.models.sleep_tracking_models

import kotlinx.serialization.Serializable


@Serializable
data class SleepTracking(
    val id: String,
    val userId: String,
    val username: String,
    val sleepStart: String?,
    val sleepEnd: String?,
    val sleepQuality: String,
    val createdAt: String?,
    val updatedAt: String?
)
