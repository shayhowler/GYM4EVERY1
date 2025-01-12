package com.gym4every1.routes.app_routes.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
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
import kotlinx.coroutines.withContext

@Composable
fun WaterTrackingPage(
    navController: NavController,
    supabaseClient: SupabaseClient,
    paddingValues: PaddingValues
) {
    var userId by remember { mutableStateOf<String?>(null) }
    var username by remember { mutableStateOf<String?>(null) }
    var waterData by remember { mutableStateOf<List<WaterTracking>>(emptyList()) }
    var showInsertDialog by remember { mutableStateOf(false) }
    var waterIntake by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // Fetch user data and water tracking records
    LaunchedEffect(Unit) {
        val session = supabaseClient.auth.currentSessionOrNull()
        if (session != null) {
            userId = session.user?.id
            username = fetchUsername(supabaseClient, userId.toString())
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
            title = "Sleep Tracking",
            themeColor = Color(0xFF80DEEA) // A calming light cyan color
        ) {
            Text(
                text = "Water Tracking",
                fontSize = 24.sp,
                color = Color(0xFF81D4FA),
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            waterData.forEach { item ->
                Text(
                    text = "Date: ${item.date}, Intake: ${item.waterIntakeMl} ml",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to show the insert dialog
            Button(onClick = { showInsertDialog = true }, modifier = Modifier.padding(16.dp)) {
                Text(text = "Add Water Intake")
            }
        }
    }

    // Insert water intake dialog
    if (showInsertDialog) {
        AlertDialog(
            onDismissRequest = { showInsertDialog = false },
            title = { Text("Add Water Intake") },
            text = {
                Column {
                    if (errorMessage.isNotEmpty()) {
                        Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    // Input for date
                    TextField(
                        value = selectedDate,
                        onValueChange = { selectedDate = it },
                        label = { Text("Date (YYYY-MM-DD)") }
                    )

                    Spacer(modifier = Modifier.height(8.dp))


// Input for water intake
                    TextField(
                        value = waterIntake,
                        onValueChange = { waterIntake = it },
                        label = { Text("Water Intake (ml)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (waterIntake.isNotEmpty() && selectedDate.isNotEmpty() && userId != null && username != null) {
                        val waterTracking = WaterTracking(
                            userId = userId!!,
                            username = username!!,
                            date = selectedDate,
                            waterIntakeMl = waterIntake.toInt()
                        )

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                insertWaterTrackingData(supabaseClient, waterTracking)
                                waterData = fetchWaterTrackingData(supabaseClient, userId!!)
                                withContext(Dispatchers.Main) {
                                    showInsertDialog = false
                                    errorMessage = ""
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    errorMessage = "Failed to add data: ${e.message}"
                                }
                            }
                        }
                    } else {
                        errorMessage = "All fields must be filled correctly!"
                    }
                }) {
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