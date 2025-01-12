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
import com.gym4every1.database.fetchUsername
import com.gym4every1.database.formatTimestamp
import com.gym4every1.database.sleep_operations.fetchSleepTrackingData
import com.gym4every1.database.sleep_operations.insertSleepTrackingData
import com.gym4every1.models.sleep_tracking_models.SleepTracking
import com.gym4every1.routes.app_routes.components.GlobalTrackingPage
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun SleepTrackingPage(navController: NavController, supabaseClient: SupabaseClient, paddingValues: PaddingValues) {
    var userId by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf<String?>(null) }
    var sleepData by remember { mutableStateOf<List<SleepTracking>>(emptyList()) }
    var showInsertDialog by remember { mutableStateOf(false) }
    var sleepStart by remember { mutableStateOf("") }
    var sleepEnd by remember { mutableStateOf("") }
    var sleepQuality by remember { mutableStateOf("") }

    // Fetch user session, username, and initial sleep data
    LaunchedEffect(true) {
        val session = supabaseClient.auth.currentSessionOrNull()
        if (session != null) {
            userId = session.user?.id
            username = fetchUsername(supabaseClient, userId!!)
            if (userId != null) {
                sleepData = fetchSleepTrackingData(supabaseClient, userId!!)
            }
        }
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val currentDateString = dateFormat.format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        GlobalTrackingPage(
            title = "Sleep Tracking",
            themeColor = Color(0xFF80DEEA) // A calming light cyan color
        ) {
            Text(
                text = "8 Hours Slept Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF00796B) // Dark teal for contrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display Sleep Data
            sleepData.forEach { item ->
                Text(
                    text = "Sleep Start: ${item.sleep_start}, Sleep End: ${item.sleep_end}, Quality: ${item.sleep_quality}",
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
                    TextField(
                        value = sleepStart,
                        onValueChange = { sleepStart = it },
                        label = { Text("Sleep Start (YYYY-MM-DD HH:MM)") },
                        placeholder = { Text(currentDateString) }
                    )

                    TextField(
                        value = sleepEnd,
                        onValueChange = { sleepEnd = it },
                        label = { Text("Sleep End (YYYY-MM-DD HH:MM)") }
                    )

                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Select Sleep Quality:")
                        val options = listOf("poor", "average", "good", "excellent")
                        options.forEach { quality ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
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
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (sleepStart.isNotBlank() && sleepEnd.isNotBlank() && sleepQuality.isNotBlank()) {
                            val sleepTracking = SleepTracking(
                                id = "",
                                user_id = userId ?: return@Button,
                                username = username ?: return@Button,
                                sleep_start = sleepStart,
                                sleep_end = sleepEnd,
                                sleep_quality = sleepQuality,
                                created_at = formatTimestamp(System.currentTimeMillis().toString()),
                                updated_at = formatTimestamp(System.currentTimeMillis().toString())
                            )

                            CoroutineScope(Dispatchers.IO).launch {
                                insertSleepTrackingData(supabaseClient, sleepTracking)
                                sleepData = fetchSleepTrackingData(supabaseClient, userId!!)
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



