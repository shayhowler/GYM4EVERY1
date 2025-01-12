package com.gym4every1.database.water_operations

import com.gym4every1.models.water_tracking_models.WaterTracking
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun fetchWaterTrackingData(supabaseClient: SupabaseClient, userId: String): List<WaterTracking> {
    return withContext(Dispatchers.IO) { // Using Dispatchers.IO to run the network request on the IO thread
        try {
            val response = supabaseClient.from("water_tracking")
                .select(columns = Columns.list("id, user_id, username, date, water_intake_ml, created_at, updated_at"))
                .decodeList<WaterTracking>() // Decoding all records from the table

            // Filter the results based on the userId after decoding
            response.filter { it.userId == userId } // Apply the filter on decoded results
        } catch (e: Exception) {
            e.printStackTrace() // Print the exception for debugging
            emptyList<WaterTracking>() // Return an empty list in case of error
        }
    }
}

suspend fun insertWaterTrackingData(supabaseClient: SupabaseClient, waterTracking: WaterTracking) {
    try {
        // Prepare the map of values to insert into Supabase
        val waterTrackingMap = mapOf(
            "user_id" to waterTracking.userId,
            "username" to waterTracking.username,
            "date" to waterTracking.date,
            "water_intake_ml" to waterTracking.waterIntakeMl,
            "created_at" to waterTracking.createdAt,
            "updated_at" to waterTracking.updatedAt
        )

        // Insert data into Supabase
        val response = supabaseClient.from("water_tracking")
            .insert(waterTrackingMap)

        // Handle the response (optional)

    } catch (e: Exception) {
        e.printStackTrace() // Handle exception as needed
    }
}