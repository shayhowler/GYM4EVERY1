package com.gym4every1.singletons

import com.gym4every1.models.workout_plans_models.ExerciseDetails

object ExerciseCache {
    private val cache = mutableMapOf<String, ExerciseDetails>()

    fun getExercise(id: String): ExerciseDetails? = cache[id]

    fun addExercise(id: String, details: ExerciseDetails) {
        cache[id] = details
    }
}