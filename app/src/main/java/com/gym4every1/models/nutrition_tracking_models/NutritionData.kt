package com.gym4every1.models.nutrition_tracking_models

import kotlinx.serialization.Serializable

@Serializable
data class NutritionData(
    val date: String,
    val mealType: String,
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val productName: String = "",
    val imageUrl: String = ""
)