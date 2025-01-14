package com.gym4every1.routes.app_routes.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gym4every1.api_integrations.exercises_api_fetch.fetchAndCacheExercise
import com.gym4every1.models.workout_plans_models.Exercise
import com.gym4every1.models.workout_plans_models.ExerciseDetails
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ExerciseTimerScreen(
    paddingValues: PaddingValues,
    exercises: List<Exercise>,
    onWorkoutComplete: () -> Unit
) {
    var currentExerciseIndex by remember { mutableIntStateOf(0) }
    var timeRemaining by remember { mutableIntStateOf(exercises[currentExerciseIndex].duration) } // Set initial duration
    var isBreakTime by remember { mutableStateOf(false) }
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch ExerciseDetails for current exercise
    var exerciseDetails by remember { mutableStateOf<ExerciseDetails?>(null) }
    val currentExercise = exercises[currentExerciseIndex]

    // Fetch details for the current exercise if not already fetched
    LaunchedEffect(currentExerciseIndex) {
        exerciseDetails = fetchAndCacheExercise(currentExercise.name) // Assuming name is used as id
    }

    // Timer logic
    LaunchedEffect(currentExerciseIndex, isBreakTime) {
        coroutineScope.launch {
            while (timeRemaining > 0) {
                delay(1000L) // 1-second delay
                timeRemaining--
            }

            if (isBreakTime) {
                isBreakTime = false
                if (currentExerciseIndex + 1 < exercises.size) {
                    currentExerciseIndex++
                    timeRemaining = exercises[currentExerciseIndex].duration // Set new exercise duration
                } else {
                    onWorkoutComplete() // End of workout
                }
            } else {
                isBreakTime = true
                timeRemaining = currentExercise.breakDuration // Set break duration
            }
        }
    }

    // Image cycling for GIF effect (no changes needed)
    LaunchedEffect(key1 = currentExerciseIndex, key2 = isBreakTime) {
        if (!isBreakTime) { // Only cycle images during exercise, not break
            while (true) {
                delay(1000L) // Change image every 1000ms (1 second)
                currentImageIndex = (currentImageIndex + 1) % 2 // Loop between 0 and 1
            }
        } else {
            // Reset the current image index during break time
            currentImageIndex = 0
        }
    }

    val currentImageUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/refs/heads/main/exercises/${currentExercise.name}/${currentImageIndex}.jpg"
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Dynamic Exercise Image Section (hide during break time)
        if (!isBreakTime) { // Only display images when it's not a break
            androidx.compose.material3.Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(16.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                AsyncImage(
                    model = currentImageUrl,
                    contentDescription = "Exercise Image",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Exercise or Break Header
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Text(
                text = if (isBreakTime) "Break Time" else currentExercise.name.replace('_', ' '),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Timer Section
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = androidx.compose.material3.CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Time Remaining: $timeRemaining seconds",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Instructions Section (only show if exercise details are loaded)
        if (!isBreakTime && exerciseDetails != null) {
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    exerciseDetails?.instructions?.forEachIndexed { index, instruction ->
                        androidx.compose.material3.Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = androidx.compose.material3.CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "${index + 1}. $instruction",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Muscles Involved Section (only show if exercise details are loaded)
        if (!isBreakTime && exerciseDetails != null) {
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Muscles Involved",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Primary Muscles: ${exerciseDetails?.primaryMuscles?.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (exerciseDetails?.secondaryMuscles?.isNotEmpty() == true) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Secondary Muscles: ${exerciseDetails?.secondaryMuscles?.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}