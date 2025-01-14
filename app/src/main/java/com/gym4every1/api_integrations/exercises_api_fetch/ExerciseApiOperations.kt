package com.gym4every1.api_integrations.exercises_api_fetch

import com.gym4every1.BuildConfig
import com.gym4every1.models.workout_plans_models.ExerciseDetails
import com.gym4every1.singletons.ExerciseCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

fun parseExerciseDetails(response: String): ExerciseDetails {
    val json = JSONObject(response)
    return ExerciseDetails(
        category = json.getString("category"),
        equipment = json.optString("equipment"),
        force = json.getString("force"),
        id = json.getString("id"),
        images = json.getJSONArray("images").let { array ->
            List(array.length()) { array.getString(it) }
        },
        instructions = json.getJSONArray("instructions").let { array ->
            List(array.length()) { array.getString(it) }
        },
        level = json.getString("level"),
        mechanic = json.optString("mechanic"),
        name = json.getString("name"),
        primaryMuscles = json.getJSONArray("primaryMuscles").let { array ->
            List(array.length()) { array.getString(it) }
        },
        secondaryMuscles = json.getJSONArray("secondaryMuscles").let { array ->
            List(array.length()) { array.getString(it) }
        }
    )
}

suspend fun fetchAndCacheExercise(exerciseId: String): ExerciseDetails? {
    // Check if the exercise is already in the cache
    val cachedExercise = ExerciseCache.getExercise(exerciseId)
    if (cachedExercise != null) {
        return cachedExercise
    }

    return withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url("https://exercise-db-fitness-workout-gym.p.rapidapi.com/exercise/$exerciseId")
                .get()
                .addHeader("x-rapidapi-key", BuildConfig.RAPID_API_KEY)
                .addHeader("x-rapidapi-host", BuildConfig.RAPID_API_HOST)
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                responseBody?.let {
                    val exerciseDetails = parseExerciseDetails(it)
                    ExerciseCache.addExercise(exerciseId, exerciseDetails) // Add to cache
                    exerciseDetails
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}