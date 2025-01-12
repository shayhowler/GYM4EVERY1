package com.gym4every1.routes.app_routes.stats

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
import com.gym4every1.database.formatTimestamp
import com.gym4every1.database.water_operations.fetchWaterTrackingData
import com.gym4every1.database.water_operations.insertWaterTrackingData
import com.gym4every1.models.water_tracking_models.WaterTracking
import com.gym4every1.routes.app_routes.components.GlobalTrackingPage
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WaterTrackingPage(navController: NavController, supabaseClient: SupabaseClient, paddingValues: PaddingValues) {
    val userId = "user_id_example" // Replace with dynamic user ID
    val username = "username_example" // Replace with dynamic username

    // Fetch Water Data
    var waterData by remember { mutableStateOf<List<WaterTracking>>(emptyList()) }
    LaunchedEffect(true) {
        waterData = fetchWaterTrackingData(supabaseClient, userId)
    }

    var showInsertDialog by remember { mutableStateOf(false) }
    var waterIntake by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        GlobalTrackingPage(
            title = "Water Tracking",
            themeColor = Color(0xFF81D4FA) // Light Blue for Water
        ) {
            Text(
                text = "6/8 Cups Drunk Today",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Water Tracking List (for display purposes)
            waterData.forEach { item ->
                Text(
                    text = "Date: ${item.date}, Intake: ${item.waterIntakeMl} ml",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add Water Entry Button
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
                    // Date input
                    TextField(
                        value = selectedDate,
                        onValueChange = { selectedDate = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )

                    // Water Intake input
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
                        if (waterIntake.isNotEmpty() && selectedDate.isNotEmpty()) {
                            val waterTracking = WaterTracking(
                                id = "", // Handle ID generation as needed
                                userId = userId,
                                username = username,
                                date = selectedDate,
                                waterIntakeMl = waterIntake.toInt(),
                                createdAt = formatTimestamp(System.currentTimeMillis().toString()),
                                updatedAt = formatTimestamp(System.currentTimeMillis().toString())
                            )

                            // Use coroutine scope for the database operation
                            CoroutineScope(Dispatchers.IO).launch {
                                insertWaterTrackingData(supabaseClient, waterTracking)
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
