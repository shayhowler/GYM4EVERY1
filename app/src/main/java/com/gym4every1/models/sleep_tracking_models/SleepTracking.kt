package com.gym4every1.models.sleep_tracking_models

import kotlinx.serialization.Serializable


@Serializable
data class SleepTracking(
    val id: String,
    val user_id: String,
    val username: String,
    val sleep_start: String?,
    val sleep_end: String?,
    val sleep_quality: String,
    val created_at: String?,
    val updated_at: String?
)
