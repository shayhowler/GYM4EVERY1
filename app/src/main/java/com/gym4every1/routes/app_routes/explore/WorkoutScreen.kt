package com.gym4every1.routes.app_routes.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.gym4every1.models.workout_plans_models.Exercise

@Composable
fun WorkoutScreen(navController: NavController, paddingValues: PaddingValues, programName: String, thumbnailUrl: String) {
    val workoutDescriptions = mapOf(
        "Cardio" to "Cardio exercises are designed to increase heart rate and improve cardiovascular fitness. This program includes activities that get you moving and burning calories.",
        "Arm Training" to "Focus on strengthening your hands and forearms with exercises designed to improve grip strength and flexibility.",
        "Lower Body Training" to "Target your legs, glutes, and lower abdomen with this program. Reduce weight and tone muscles even without using tools.",
        "Back Training" to "Strengthen your back muscles and improve posture with exercises that target your upper and lower back.",
        "Chest Training" to "Enhance chest strength and definition with targeted exercises that improve both form and function.",
        "Core Training" to "Engage your core muscles with a variety of exercises aimed at improving stability, balance, and overall strength."
    )

    val description = workoutDescriptions[programName] ?: "No description available for this program."

    var exercises by remember { mutableStateOf<List<Exercise>?>(null) }
    var workoutFinished by remember { mutableStateOf(false) }

    LaunchedEffect(programName) {
        exercises = fetchWorkoutProgram(programName)
    }

    if (workoutFinished) {
        Text("You have successfully finished the workout!")
        return
    }

    if (exercises == null) {
        Text("Loading program...")
        return
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
    ) {
        // Top Section with Header Image and Info
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                InfoCard(icon = Icons.Default.Star, text = "20 min")
                Spacer(modifier = Modifier.width(8.dp))
                InfoCard(icon = Icons.Default.Face, text = "95 kcal")
            }
        }

        // Title and Description
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = programName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Rounds Section
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Rounds",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))

        exercises?.forEachIndexed { index, exercise ->
            ExerciseCard(
                navController,
                exercise = exercise, index = index)
        }

        // Button
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("exerciseTimer/${programName}")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Let's Workout")
        }
    }
}

@Composable
fun InfoCard(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
@Composable
fun ExerciseCard(navController: NavController ,exercise: Exercise, index: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { navController.navigate("exerciseDetails/${exercise.name}") }, // Navigate on click with exercise name as argument
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            val imageUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/refs/heads/main/exercises/${exercise.name}/0.jpg"
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = "${index + 1}. ${exercise.name.replace('_', ' ')}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "${exercise.duration}s", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}