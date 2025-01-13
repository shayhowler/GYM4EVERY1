package com.gym4every1.models.workout_plans_models

data class Exercise(
    val name: String,
    val duration: Int, // Duration in seconds
    val breakDuration: Int = 0 // Duration of rest breaks
)