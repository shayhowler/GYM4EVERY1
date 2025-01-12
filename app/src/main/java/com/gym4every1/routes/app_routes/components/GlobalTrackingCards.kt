package com.gym4every1.routes.app_routes.components

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GlobalTrackingPage(
    title: String,
    themeColor: Color,
    content: @Composable (selectedDate: Date) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(Date()) }
    val calendar = Calendar.getInstance()
    calendar.time = selectedDate

    // Set first day of the week to Monday
    calendar.firstDayOfWeek = Calendar.MONDAY

    // Getting selected day, shift the calendar to start from Monday (1 = Monday, 7 = Sunday)
    var selectedDay = calendar.get(Calendar.DAY_OF_WEEK)
    if (selectedDay == Calendar.SUNDAY) {
        selectedDay = 7  // Make Sunday = 7
    } else {
        selectedDay -= 1 // Shift days so Monday = 1, Tuesday = 2, ..., Sunday = 7
    }

    // Formatting the date
    val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

    // Weekday names (Starting from Monday)
    val weekDays = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = themeColor)
    ) {
        // Header with Week Days and Date Picker
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Week Days Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                weekDays.forEachIndexed { index, day ->
                    val dayColor = if (selectedDay == index + 1) Color.White else Color.Gray
                    Text(
                        text = day,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = dayColor,
                        modifier = Modifier
                            .clickable {
                                // Correct day setting logic for Sunday (7) and others
                                if (index == 6) {
                                    // If Sunday is clicked, don't reset to Monday, keep it as Sunday
                                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                                } else {
                                    // For other days, adjust properly
                                    calendar.set(Calendar.DAY_OF_WEEK, index + 2) // Set the selected day
                                }
                                selectedDate = calendar.time
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date Picker Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Selected Date: ${dateFormat.format(selectedDate)}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = "Change Date",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Yellow,
                    modifier = Modifier.clickable {
                        // Show Date Picker
                        val datePicker = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                selectedDate = calendar.time
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePicker.show()
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Page-Specific Content
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White, // Card background color
                contentColor = MaterialTheme.colorScheme.onSurface // Content text color
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = themeColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                content(selectedDate) // Pass the selectedDate to the content
            }
        }
    }
}




