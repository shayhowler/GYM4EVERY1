package com.gym4every1.database.sleep_operations

import com.gym4every1.models.sleep_tracking_models.SleepTracking
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

suspend fun fetchSleepTrackingData(supabaseClient: SupabaseClient, userId: String): List<SleepTracking> {
    return withContext(Dispatchers.IO) {
        try {
            // Fetch data from Supabase
            val response = supabaseClient.from("sleep_tracking")
                .select(columns = Columns.list("id, user_id, username, sleep_start, sleep_end, sleep_quality, created_at, updated_at"))
                .decodeList<SleepTracking>() // Decode into a list of SleepTracking models

            // Filter by user ID after decoding
            response.filter { it.user_id == userId }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // Return an empty list if an error occurs
        }
    }
}




suspend fun insertSleepTrackingData(supabaseClient: SupabaseClient, sleepTracking: SleepTracking) {
    try {
        val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
        supabaseClient.from("sleep_tracking")
            .insert(
                mapOf(
                    "user_id" to sleepTracking.user_id,
                    "username" to sleepTracking.username,
                    "sleep_start" to sleepTracking.sleep_start,
                    "sleep_end" to sleepTracking.sleep_end,
                    "sleep_quality" to sleepTracking.sleep_quality,
                    "created_at" to timestamp,
                    "updated_at" to timestamp
                )
            )
    } catch (e: Exception) {
        e.printStackTrace() // Handle exception as needed
    }
}
