package com.gym4every1.database.water_operations

import com.gym4every1.database.formatTimestamp
import com.gym4every1.models.water_tracking_models.WaterTracking
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

// Fetch water tracking data based on user ID
suspend fun fetchWaterTrackingData(supabaseClient: SupabaseClient, userId: String): List<WaterTracking> {
    return withContext(Dispatchers.IO) { // Using Dispatchers.IO for network operations
        try {
            val response = supabaseClient.from("water_tracking")
                .select(columns = Columns.list("id, user_id, username, date, water_intake_ml, created_at, updated_at"))
                .decodeList<WaterTracking>() // Decoding all records into a list

            // Filter results by userId
            response.filter { it.user_id == userId }
        } catch (e: Exception) {
            e.printStackTrace() // Log the error for debugging
            emptyList() // Return an empty list in case of an error
        }
    }
}

// Insert water tracking data with proper formatting
suspend fun insertWaterTrackingData(supabaseClient: SupabaseClient, waterTracking: WaterTracking) {
    try {
        // Prepare the date and time fields using the formatTimestamp function
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").apply {
            timeZone = TimeZone.getTimeZone("UTC") // Ensuring UTC format
        }.format(Date()) // Create a current timestamp for `created_at` and `updated_at`

        val waterTrackingMap = mapOf(
            "user_id" to waterTracking.user_id,
            "username" to waterTracking.username,
            "date" to waterTracking.date?.takeIf { it.isNotEmpty() }, // Only insert if date is not empty
            "water_intake_ml" to waterTracking.water_intake_ml.takeIf { it > 0 }, // Only insert if valid water intake
            "created_at" to timestamp,
            "updated_at" to timestamp
        )

        // Perform the insert operation
        withContext(Dispatchers.IO) {
            val response = supabaseClient.from("water_tracking")
                .insert(waterTrackingMap)
        }
    } catch (e: Exception) {
        e.printStackTrace() // Handle the error appropriately
    }
}
