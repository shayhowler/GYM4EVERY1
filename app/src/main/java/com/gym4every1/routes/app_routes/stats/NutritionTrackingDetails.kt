package com.gym4every1.routes.app_routes.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gym4every1.routes.app_routes.components.GlobalTrackingPage
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NutritionTrackingPage(
    navController: NavController,
    paddingValues: PaddingValues
) {
    val selectedDate = remember { mutableStateOf(Date()) }
    val nutritionData = remember { mutableStateOf(mockNutritionData.toMutableList()) } // Mutable list for editing data
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snacks/Other")

    GlobalTrackingPage(
        title = "Nutrition Tracking",
        themeColor = Color(0xFF80DEEA) // Light cyan theme
    ) { date ->
        selectedDate.value = date
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            mealTypes.forEach { mealType ->
                val mealsForType = nutritionData.value.filter {
                    it.date == SimpleDateFormat("yyyy-MM-dd").format(selectedDate.value) && it.mealType == mealType
                }
                MealCard(
                    mealType = mealType,
                    meals = mealsForType,
                    onAddMeal = { newMeal ->
                        nutritionData.value = (nutritionData.value + newMeal).toMutableList()
                    }
                )
            }
        }
    }
}

@Composable
fun MealCard(
    mealType: String,
    meals: List<NutritionData>,
    onAddMeal: (NutritionData) -> Unit
) {
    var showMealDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)) // Light gray background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mealType,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Button(
                    onClick = { showMealDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF80DEEA)) // Match the theme
                ) {
                    Text(text = "Add Meal")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn {
                items(meals.size) { index ->
                    val meal = meals[index]
                    Text(
                        text = "${meal.calories} Cal | ${meal.carbs}g Carbs | ${meal.protein}g Protein | ${meal.fat}g Fat",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }
    }

    if (showMealDialog) {
        AddMealDialog(
            mealType = mealType,
            onDismiss = { showMealDialog = false },
            onAddMeal = { newMeal ->
                showMealDialog = false
                onAddMeal(newMeal)
            }
        )
    }
}

@Composable
fun AddMealDialog(
    mealType: String,
    onDismiss: () -> Unit,
    onAddMeal: (NutritionData) -> Unit
) {
    var calories by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(text = "Add $mealType")
        },
        text = {
            Column {
                TextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories") }
                )
                TextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbs (g)") }
                )
                TextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein (g)") }
                )
                TextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (g)") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newMeal = NutritionData(
                        date = SimpleDateFormat("yyyy-MM-dd").format(Date()),
                        mealType = mealType,
                        calories = calories.toIntOrNull() ?: 0,
                        carbs = carbs.toFloatOrNull() ?: 0f,
                        protein = protein.toFloatOrNull() ?: 0f,
                        fat = fat.toFloatOrNull() ?: 0f
                    )
                    onAddMeal(newMeal)
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

data class NutritionData(
    val date: String,
    val mealType: String,
    val calories: Int,
    val carbs: Float,
    val protein: Float,
    val fat: Float
)

val mockNutritionData = listOf(
    NutritionData("2025-01-13", "Breakfast", 200, 30f, 10f, 5f),
    NutritionData("2025-01-13", "Lunch", 500, 60f, 25f, 20f),
    NutritionData("2025-01-13", "Dinner", 600, 70f, 30f, 25f),
    NutritionData("2025-01-13", "Snacks/Other", 300, 40f, 15f, 10f)
)
