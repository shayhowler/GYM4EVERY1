package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: String,           // Unique ID for the message
    val sender_id: String,    // ID of the user sending the message
    val receiver_id: String,  // ID of the user receiving the message
    val content: String,      // Message content
    val media_url: String?,   // Media URL (optional)
    val status: String,       // Message status (sent, delivered, read)
    val created_at: String,   // Timestamp of creation
    val updated_at: String    // Timestamp of the last update
)