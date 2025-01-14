package com.gym4every1.models.nutrition_tracking_models

data class NutritionData(
    val date: String,
    val mealType: String,
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float,
    val productName: String = "",  // Added product name for display
    val imageUrl: String = ""  // Added image URL for displaying product image
)
