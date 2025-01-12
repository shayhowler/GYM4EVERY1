package com.gym4every1.database.sleep_operations

import com.gym4every1.models.sleep_tracking_models.SleepTracking
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun fetchSleepTrackingData(supabaseClient: SupabaseClient, userId: String): List<SleepTracking> {
    return withContext(Dispatchers.IO) { // Using Dispatchers.IO to run the network request on the IO thread
        try {
            val response = supabaseClient.from("sleep_tracking")
                .select(columns = Columns.list("id, user_id, username, sleep_start, sleep_end, sleep_quality, created_at, updated_at"))
                .decodeList<SleepTracking>() // Decoding all records from the table

            // Filter the results based on the userId after decoding
            response.filter { it.userId == userId } // Apply the filter on decoded results
        } catch (e: Exception) {
            e.printStackTrace() // Print the exception for debugging
            emptyList<SleepTracking>() // Return an empty list in case of error
        }
    }
}



suspend fun insertSleepTrackingData(supabaseClient: SupabaseClient, sleepTracking: SleepTracking) {
    try {
        supabaseClient.from("sleep_tracking")
            .insert(
                mapOf(
                    "user_id" to sleepTracking.userId,
                    "username" to sleepTracking.username,
                    "sleep_start" to sleepTracking.sleepStart,
                    "sleep_end" to sleepTracking.sleepEnd,
                    "sleep_quality" to sleepTracking.sleepQuality,
                    "created_at" to sleepTracking.createdAt,
                    "updated_at" to sleepTracking.updatedAt
                )
            )
    } catch (e: Exception) {
        e.printStackTrace() // Handle exception as needed
    }
}