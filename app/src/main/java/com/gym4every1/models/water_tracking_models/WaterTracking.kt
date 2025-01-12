package com.gym4every1.models.water_tracking_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaterTracking(
    val id: String? = null, // Make id nullable and optional
    @SerialName("user_id") val userId: String,
    @SerialName("username") val username: String,
    @SerialName("date") val date: String, // Non-nullable
    @SerialName("water_intake_ml") val waterIntakeMl: Int,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null
)