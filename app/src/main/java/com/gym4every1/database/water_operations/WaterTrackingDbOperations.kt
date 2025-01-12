package com.gym4every1.database.water_operations

import com.gym4every1.models.water_tracking_models.WaterTracking
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Fetch water tracking data based on user ID
suspend fun fetchWaterTrackingData(
    supabaseClient: SupabaseClient,
    userId: String,
    selectedDate: Date
): List<WaterTracking> {
    return withContext(Dispatchers.IO) {
        try {
            // Format the selected date to match the database format (yyyy-MM-dd)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate)

            val response = supabaseClient.from("water_tracking")
                .select(columns = Columns.list("id, user_id, username, date, water_intake_ml, created_at, updated_at"))
                .decodeList<WaterTracking>()

            println("Fetched Data: $response") // Debugging

            // Filter results by userId and selected date
            response.filter { it.userId == userId && it.date == formattedDate }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list in case of an error
        }
    }
}
// Insert water tracking data with proper formatting
suspend fun insertWaterTrackingData(supabaseClient: SupabaseClient, waterTracking: WaterTracking) {
    try {
        withContext(Dispatchers.IO) {
            supabaseClient.from("water_tracking")
                .insert(waterTracking) // Insert the data class directly
        }
        println("Data inserted successfully: $waterTracking")
    } catch (e: Exception) {
        e.printStackTrace() // Handle the error appropriately
    }
}