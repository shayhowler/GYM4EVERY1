package com.gym4every1.routes.app_routes.stats

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gym4every1.database.fetchUsername
import com.gym4every1.database.water_operations.fetchWaterTrackingData
import com.gym4every1.database.water_operations.insertWaterTrackingData
import com.gym4every1.models.water_tracking_models.WaterTracking
import com.gym4every1.routes.app_routes.components.GlobalTrackingPage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WaterTrackingPage(navController: NavController, supabaseClient: SupabaseClient, paddingValues: PaddingValues) {
    var userId by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf<String?>(null) }
    var waterData by remember { mutableStateOf<List<WaterTracking>>(emptyList()) }
    var showInsertDialog by remember { mutableStateOf(false) }
    var waterIntake by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Fetch user session, username, and initial water tracking data
    LaunchedEffect(true) {
        val session = supabaseClient.auth.currentSessionOrNull()
        if (session != null) {
            userId = session.user?.id
            username = userId?.let { fetchUsername(supabaseClient, it) }
            if (userId != null) {
                waterData = fetchWaterTrackingData(supabaseClient, userId!!)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        GlobalTrackingPage(
            title = "Water Tracking",
            themeColor = Color(0xFF81D4FA)
        ) {
            Text(
                text = "6/8 Cups Drunk Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display Water Tracking List
            waterData.forEach { item ->
                val formattedDate = item.date ?: "Unknown Date"
                Text(
                    text = "Date: $formattedDate, Intake: ${item.water_intake_ml} ml",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Water Intake Button
            Button(onClick = { showInsertDialog = true }) {
                Text(text = "Add Water Intake")
            }
        }
    }

    // Water Intake Dialog
    if (showInsertDialog) {
        AlertDialog(
            onDismissRequest = { showInsertDialog = false },
            title = { Text("Add Water Intake") },
            text = {
                Column {
                    // Error Message
                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Date Input
                    TextField(
                        value = selectedDate,
                        onValueChange = { selectedDate = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )

                    // Water Intake Input
                    TextField(
                        value = waterIntake,
                        onValueChange = { waterIntake = it },
                        label = { Text("Water Intake (ml)") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedDate = parseDate(selectedDate)
                        if (waterIntake.isNotEmpty() && parsedDate != null && userId != null && username != null) {
                            try {
                                val intake = waterIntake.toInt()
                                if (intake <= 0) {
                                    errorMessage = "Water intake must be a positive number"
                                    return@Button
                                }
                                val waterTracking = WaterTracking(
                                    id = "", // Handle ID generation in Supabase
                                    user_id = userId!!,
                                    username = username!!,
                                    date = formatDate(parsedDate), // Format date before saving
                                    water_intake_ml = intake,
                                    created_at = null, // Supabase will auto-generate
                                    updated_at = null // Supabase will auto-generate
                                )

                                // Insert data and refresh list
                                CoroutineScope(Dispatchers.IO).launch {
                                    insertWaterTrackingData(supabaseClient, waterTracking)
                                    waterData = fetchWaterTrackingData(supabaseClient, userId!!)
                                }
                                showInsertDialog = false
                                errorMessage = ""
                            } catch (e: NumberFormatException) {
                                errorMessage = "Invalid water intake amount"
                            }
                        } else {
                            errorMessage = "Please fill all fields with valid values"
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = {
                    showInsertDialog = false
                    errorMessage = ""
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Utility function to parse a String into a Date
fun parseDate(dateString: String): Date? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(dateString)
    } catch (e: Exception) {
        null
    }
}

// Utility function to format a Date into a String
fun formatDate(date: Date): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(date)
}
