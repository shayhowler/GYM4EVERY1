package com.gym4every1.models.water_tracking_models

import kotlinx.serialization.Serializable

@Serializable
data class WaterTracking(
    val id: String,
    val user_id: String,
    val username: String,
    val date: String, // Ensure this is formatted as "yyyy-MM-dd"
    val water_intake_ml: Int,
    val created_at: String?,
    val updated_at: String?
)
