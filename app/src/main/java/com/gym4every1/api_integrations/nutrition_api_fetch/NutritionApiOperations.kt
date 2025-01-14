package com.gym4every1.api_integrations.nutrition_api_fetch

import com.gym4every1.models.nutrition_tracking_models.NutritionData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date

fun fetchNutritionDataFromAPI(query: String, onResult: (List<NutritionData>) -> Unit) {
    // Use Coroutine to fetch data asynchronously
    CoroutineScope(Dispatchers.IO).launch {
        val url = "https://world.openfoodfacts.org/cgi/search.pl?search_terms=$query&fields=product_name,nutriments,image_thumb_url&json=1"
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val jsonResponse = JSONObject(response.body?.string())
                val products = jsonResponse.getJSONArray("products")

                val nutritionList = mutableListOf<NutritionData>()
                for (i in 0 until products.length()) {
                    val product = products.getJSONObject(i)
                    val productName = product.optString("product_name", "Unknown Product")
                    val imageUrl = product.optString("image_thumb_url", "")
                    val nutriments = product.optJSONObject("nutriments")

                    val calories = nutriments?.optInt("energy-kcal_100g", 0) ?: 0
                    val carbs = nutriments?.optDouble("carbohydrates_100g", 0.0) ?: 0.0
                    val protein = nutriments?.optDouble("proteins_100g", 0.0) ?: 0.0
                    val fat = nutriments?.optDouble("fat_100g", 0.0) ?: 0.0

                    // Create a NutritionData object and add it to the list
                    nutritionList.add(
                        NutritionData(
                            date = SimpleDateFormat("yyyy-MM-dd").format(Date()),
                            mealType = "Search Result", // Defaulting meal type to "Search Result"
                            calories = calories,
                            carbs = carbs.toFloat(),
                            protein = protein.toFloat(),
                            fat = fat.toFloat(),
                            productName = productName,
                            imageUrl = imageUrl
                        )
                    )
                }

                // Return the data through the callback
                withContext(Dispatchers.Main) {
                    onResult(nutritionList)
                }
            } else {
                // Handle the case where the response is not successful
                withContext(Dispatchers.Main) {
                    onResult(emptyList())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onResult(emptyList())
            }
        }
    }
}