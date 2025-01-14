package com.gym4every1.models.workout_plans_models

data class ExerciseDetails(
    val category: String,
    val equipment: String?,
    val force: String,
    val id: String,
    val images: List<String>,
    val instructions: List<String>,
    val level: String,
    val mechanic: String?,
    val name: String,
    val primaryMuscles: List<String>,
    val secondaryMuscles: List<String>
)