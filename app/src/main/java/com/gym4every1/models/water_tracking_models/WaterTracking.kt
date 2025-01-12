package com.gym4every1.models.water_tracking_models

import kotlinx.serialization.Serializable

@Serializable
data class WaterTracking(
    val id: String,
    val userId: String,
    val username: String,
    val date: String?,
    val waterIntakeMl: Int,
    val createdAt: String?,
    val updatedAt: String?
)
