package com.gym4every1.routes.app_routes.stats

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.gym4every1.api_integrations.nutrition_api_fetch.fetchNutritionDataFromAPI
import com.gym4every1.models.nutrition_tracking_models.NutritionData
import com.gym4every1.routes.app_routes.components.GlobalTrackingPage

object NutritionState {
    val selectedMeals = mutableStateOf<Map<String, List<NutritionData>>>(emptyMap())
    val totalNutrients = mutableStateOf<Map<String, Float>>(emptyMap())

    fun addMeal(mealType: String, nutritionData: NutritionData) {
        val updatedMeals = selectedMeals.value.toMutableMap()
        val mealList = updatedMeals.getOrDefault(mealType, emptyList()).toMutableList()
        mealList.add(nutritionData)
        updatedMeals[mealType] = mealList
        selectedMeals.value = updatedMeals

        totalNutrients.value = calculateTotalNutrients(updatedMeals)
    }

    private fun calculateTotalNutrients(meals: Map<String, List<NutritionData>>): Map<String, Float> {
        return meals.values.flatten().fold(mapOf()) { acc, nutrition ->
            acc + mapOf(
                "calories" to (acc["calories"] ?: 0f) + nutrition.calories,
                "carbs" to (acc["carbs"] ?: 0f) + nutrition.carbs,
                "protein" to (acc["protein"] ?: 0f) + nutrition.protein,
                "fat" to (acc["fat"] ?: 0f) + nutrition.fat
            )
        }
    }

    // Function to calculate nutrients for a specific meal
    fun calculateMealNutrients(mealItems: List<NutritionData>): Map<String, Float> {
        return mealItems.fold(mapOf<String, Float>()) { acc, nutrition ->
            acc + mapOf(
                "calories" to (acc["calories"] ?: 0f) + nutrition.calories,
                "carbs" to (acc["carbs"] ?: 0f) + nutrition.carbs,
                "protein" to (acc["protein"] ?: 0f) + nutrition.protein,
                "fat" to (acc["fat"] ?: 0f) + nutrition.fat
            )
        }
    }
}

@Composable
fun NutritionTrackingPage(navController: NavHostController) {
    val selectedMeals = NutritionState.selectedMeals.value
    val totalNutrients = NutritionState.totalNutrients.value
    val mealTypes = listOf("Breakfast", "Lunch", "Dinner", "Snacks/Other")
    Column(modifier = Modifier.fillMaxSize()) {
        GlobalTrackingPage(
            title = "Your daily nutrients",
            themeColor = Color(0xFF80DEEA) // A calming light cyan color
        ) {
            // Display total daily nutrients
            Text(
                text = "Total Nutrients:\nCalories: ${totalNutrients["calories"] ?: 0f} | " +
                        "Carbs: ${totalNutrients["carbs"] ?: 0f}g | " +
                        "Protein: ${totalNutrients["protein"] ?: 0f}g | " +
                        "Fat: ${totalNutrients["fat"] ?: 0f}g",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Meal cards
            mealTypes.forEach { mealType ->
                val mealNutrients =
                    NutritionState.calculateMealNutrients(selectedMeals[mealType] ?: emptyList())
                MealTypeCard(
                    mealType = mealType,
                    totalNutrients = mealNutrients,
                    onClick = { navController.navigate("meal_details/$mealType") }
                )
            }
        }
    }
}


@Composable
fun MealDetailsScreen(mealType: String) {
    val selectedMeals = NutritionState.selectedMeals.value[mealType] ?: emptyList()
    val totalNutrients = NutritionState.calculateMealNutrients(selectedMeals)
    var showSearchDialog by remember { mutableStateOf(false) }
    val searchResults = remember { mutableStateOf<List<NutritionData>>(emptyList()) }
    val isLoading = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        GlobalTrackingPage(
            title = "$mealType Details",
            themeColor = Color(0xFF80DEEA) // A calming light cyan color
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Total nutrients for the meal
            Text(
                text = "Total Nutrients:\nCalories: ${totalNutrients["calories"] ?: 0f} | " +
                        "Carbs: ${totalNutrients["carbs"] ?: 0f}g | " +
                        "Protein: ${totalNutrients["protein"] ?: 0f}g | " +
                        "Fat: ${totalNutrients["fat"] ?: 0f}g",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Meal items
            if (selectedMeals.isEmpty()) {
                Text("No items added to this meal yet.")
            } else {
                LazyColumn {
                    items(selectedMeals) { item ->
                        MealItemCard(item)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Add button
            Button(onClick = { showSearchDialog = true }) {
                Text("Add Item")
            }

            // Search Dialog
            if (showSearchDialog) {
                SearchDialog(
                    searchQuery = remember { mutableStateOf("") },
                    onSearch = { query ->
                        isLoading.value = true
                        fetchNutritionDataFromAPI(query) { result ->
                            searchResults.value = result
                            isLoading.value = false
                        }
                    },
                    nutritionData = searchResults.value,
                    onSelectProduct = { nutritionData ->
                        NutritionState.addMeal(mealType, nutritionData)
                        showSearchDialog = false
                    },
                    onDismiss = { showSearchDialog = false },
                    isLoading = isLoading
                )
            }
        }
    }
}
@Composable
fun MealItemCard(item: NutritionData) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.productName,
                modifier = Modifier.size(100.dp)
            )
            Text(text = item.productName)
            Text(text = "Calories: ${item.calories}")
            Text(text = "Carbs: ${item.carbs}g")
            Text(text = "Protein: ${item.protein}g")
            Text(text = "Fat: ${item.fat}g")
        }
    }
}

@Composable
fun MealTypeCard(
    mealType: String,
    totalNutrients: Map<String, Float>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = mealType, style = MaterialTheme.typography.titleMedium)
            val totalCalories = totalNutrients["calories"] ?: 0f
            val totalCarbs = totalNutrients["carbs"] ?: 0f
            val totalProtein = totalNutrients["protein"] ?: 0f
            val totalFat = totalNutrients["fat"] ?: 0f
            Text(
                text = "Calories: $totalCalories | Carbs: $totalCarbs g | Protein: $totalProtein g | Fat: $totalFat g",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun SearchDialog(
    onDismiss: () -> Unit,
    searchQuery: MutableState<String>,
    onSearch: (String) -> Unit,
    nutritionData: List<NutritionData>, // List of NutritionData
    onSelectProduct: (NutritionData) -> Unit, // Callback for selecting a product
    isLoading: MutableState<Boolean>
) {

    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier
                .padding(16.dp)
                .clickable { focusManager.clearFocus() }) {
                OutlinedTextField(
                    value = searchQuery.value,
                    onValueChange = { searchQuery.value = it },
                    label = { Text("Search for Product") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onSearch(searchQuery.value) },
                    enabled = searchQuery.value.isNotEmpty()
                ) {
                    Text("Search")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Show loading indicator or search results
                if (isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (nutritionData.isNotEmpty()) {
                    SearchResults(
                        nutritionData = nutritionData,
                        onSelectProduct = onSelectProduct,
                        onDismiss = onDismiss
                    )
                } else {
                    Text("No results found.")
                }
            }
        }
    }
}
@Composable
fun SearchResults(
    nutritionData: List<NutritionData>,
    onSelectProduct: (NutritionData) -> Unit,
    onDismiss: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(nutritionData) { nutrition ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    AsyncImage(
                        model = nutrition.imageUrl,
                        contentDescription = nutrition.productName,
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        text = nutrition.productName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Calories: ${nutrition.calories} | Carbs: ${nutrition.carbs}g | Protein: ${nutrition.protein}g | Fat: ${nutrition.fat}g",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    // ... (Rest of your Card content) ...
                    Button(onClick = {
                        onSelectProduct(nutrition)
                        onDismiss()
                    }) {
                        Text("Add to Meal")
                    }
                }
            }
        }
    }
}