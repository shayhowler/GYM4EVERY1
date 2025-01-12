package com.gym4every1.routes.app_routes.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gym4every1.database.formatTimestamp
import com.gym4every1.database.sleep_operations.fetchSleepTrackingData
import com.gym4every1.database.sleep_operations.insertSleepTrackingData
import com.gym4every1.models.sleep_tracking_models.SleepTracking
import com.gym4every1.routes.app_routes.components.GlobalTrackingPage
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.*


@Composable
fun SleepTrackingPage(navController: NavController,supabaseClient: SupabaseClient, paddingValues: PaddingValues ) {
    val userId = "user_id_example" // Replace with dynamic user ID
    val username = "username_example" // Replace with dynamic username

    // Fetch Sleep Data
    var sleepData by remember { mutableStateOf<List<SleepTracking>>(emptyList()) }
    LaunchedEffect(true) {
        sleepData = fetchSleepTrackingData(supabaseClient, userId)
    }

    var showInsertDialog by remember { mutableStateOf(false) }
    var sleepStart by remember { mutableStateOf("") }
    var sleepEnd by remember { mutableStateOf("") }
    var sleepQuality by remember { mutableStateOf("") }

    // Handle selected date for SleepTrackingPage
    val calendar = Calendar.getInstance()
    var selectedDate by remember { mutableStateOf(calendar.time) }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDateString = dateFormat.format(selectedDate)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        GlobalTrackingPage(
            title = "Sleep Tracking",
            themeColor = Color(0xFF80DEEA) // A calming light cyan color for better contrast and brightness
        ) {
            Text(
                text = "8 Hours Slept Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF00796B) // Dark teal for better contrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display Sleep Data
            sleepData.forEach { item ->
                Text(
                    text = "Sleep Start: ${item.sleepStart}, Sleep End: ${item.sleepEnd}, Quality: ${item.sleepQuality}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Sleep Entry Button
            Button(onClick = { showInsertDialog = true }) {
                Text(text = "Add Sleep Data")
            }
        }
    }

    // Sleep Tracking Dialog
    if (showInsertDialog) {
        AlertDialog(
            onDismissRequest = { showInsertDialog = false },
            title = { Text("Add Sleep Entry") },
            text = {
                Column {
                    // Sleep Start input
                    TextField(
                        value = sleepStart,
                        onValueChange = { sleepStart = it },
                        label = { Text("Sleep Start (YYYY-MM-DD HH:MM)") },
                        placeholder = { Text(currentDateString) } // Pre-fill with the selected date
                    )

                    // Sleep End input
                    TextField(
                        value = sleepEnd,
                        onValueChange = { sleepEnd = it },
                        label = { Text("Sleep End (YYYY-MM-DD HH:MM)") }
                    )

                    // Sleep Quality Selection (Radio Buttons)
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Sleep Quality:")
                        val sleepQualityOptions = listOf("poor", "average", "good", "excellent")

                        sleepQualityOptions.forEach { quality ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = sleepQuality == quality,
                                    onClick = { sleepQuality = quality }
                                )
                                Text(
                                    text = quality,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (sleepStart.isNotEmpty() && sleepEnd.isNotEmpty() && sleepQuality.isNotEmpty()) {
                            val sleepTracking = SleepTracking(
                                id = "", // Handle ID generation as needed
                                userId = userId,
                                username = username,
                                sleepStart = sleepStart,
                                sleepEnd = sleepEnd,
                                sleepQuality = sleepQuality,
                                createdAt = formatTimestamp(System.currentTimeMillis().toString()),
                                updatedAt = formatTimestamp(System.currentTimeMillis().toString())
                            )

                            // Insert sleep data using coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                insertSleepTrackingData(supabaseClient, sleepTracking)
                            }
                            showInsertDialog = false
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showInsertDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


