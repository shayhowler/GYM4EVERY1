package com.gym4every1.database.nutrition_operations

import com.gym4every1.models.nutrition_tracking_models.NutritionData
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fetch nutrition tracking data by user ID and date
suspend fun fetchNutritionTrackingData(supabaseClient: SupabaseClient, userId: String, selectedDate: Date): List<NutritionData> {
    return withContext(Dispatchers.IO) {
        try {
            // Format the selected date to yyyy-MM-dd
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            // Fetch raw data from the "nutrition_tracking" table
            val response = supabaseClient.from("nutrition_tracking")
                .select(columns = Columns.list("id, user_id, username, date, meals, total_calories, created_at, updated_at"))
                .decodeList<Map<String, String>>()  // Decoding into a map with 'Any' to handle JSONB

            response.filter { it["user_id"] == userId && it["date"] == formattedDate }
                .flatMap { record ->
                    val mealsJson = record["meals"]?.toString() ?: "[]"
                    val jsonArray = Json.parseToJsonElement(mealsJson) as? JsonArray ?: JsonArray(emptyList())
                    jsonArray.map { meal ->
                        val mealObj = meal.jsonObject
                        NutritionData(
                            date = formattedDate,
                            mealType = mealObj["mealType"]?.jsonPrimitive?.content ?: "",
                            calories = mealObj["calories"]?.jsonPrimitive?.intOrNull ?: 0,
                            carbs = mealObj["carbs"]?.jsonPrimitive?.floatOrNull ?: 0f,
                            protein = mealObj["protein"]?.jsonPrimitive?.floatOrNull ?: 0f,
                            fat = mealObj["fat"]?.jsonPrimitive?.floatOrNull ?: 0f,
                            productName = mealObj["productName"]?.jsonPrimitive?.content ?: "",
                            imageUrl = mealObj["imageUrl"]?.jsonPrimitive?.content ?: ""
                        )
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list if an error occurs
        }
    }
}

suspend fun insertNutritionTrackingData(
    supabaseClient: SupabaseClient,
    userId: String,
    username: String,
    selectedDate: Date,
    nutritionData: List<NutritionData>
) {
    try {
        withContext(Dispatchers.IO) {
            // Format date for database
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            // Serialize meals data to JSON string
            val mealsJson = Json.encodeToString(ListSerializer(NutritionData.serializer()), nutritionData)

            // Check if a record already exists for the user and date
            val existingRecords = supabaseClient.from("nutrition_tracking")
                .select(columns = Columns.list("id, user_id, date"))
                .decodeList<Map<String, String>>() // Decode the response as a list of maps
                .any { it["user_id"] == userId && it["date"] == formattedDate }
            if (existingRecords) {
                // Update existing record
                supabaseClient.from("nutrition_tracking")
                    .update(
                        mapOf(
                            "meals" to mealsJson,
                            "total_calories" to nutritionData.sumOf { it.calories }
                        )
                    )
            } else {
                // Insert new record
                supabaseClient.from("nutrition_tracking")
                    .insert(
                        mapOf(
                            "user_id" to userId,
                            "username" to username,
                            "date" to formattedDate,
                            "meals" to mealsJson, // Use JSON string for meals
                            "total_calories" to nutritionData.sumOf { it.calories }
                        )
                    )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace() // Handle exception
    }
}
