package com.gym4every1.models.social_models

import kotlinx.serialization.Serializable

@Serializable
data class MessageReaction(
    val id: String,           // Unique ID for the reaction
    val message_id: String,   // ID of the message being reacted to
    val user_id: String,      // ID of the user reacting
    val reaction_emoji: String, // Emoji used for the reaction
    val created_at: String    // Timestamp of when the reaction was made
)