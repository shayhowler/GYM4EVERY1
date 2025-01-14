package com.gym4every1.routes.navigation_handling

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.gym4every1.models.workout_plans_models.Exercise
import com.gym4every1.routes.app_routes.components.BottomNavigationBar
import com.gym4every1.routes.app_routes.components.TopBar
import com.gym4every1.routes.app_routes.explore.ExerciseDetailsScreen
import com.gym4every1.routes.app_routes.explore.ExerciseTimerScreen
import com.gym4every1.routes.app_routes.explore.ExploreScreen
import com.gym4every1.routes.app_routes.explore.WorkoutScreen
import com.gym4every1.routes.app_routes.explore.fetchWorkoutProgram
import com.gym4every1.routes.app_routes.feed.FeedScreen
import com.gym4every1.routes.app_routes.profile.ProfileScreen
import com.gym4every1.routes.app_routes.stats.MealDetailsScreen
import com.gym4every1.routes.app_routes.stats.NutritionTrackingPage
import com.gym4every1.routes.app_routes.stats.SleepTrackingPage
import com.gym4every1.routes.app_routes.stats.StatsScreen
import com.gym4every1.routes.app_routes.stats.WaterTrackingPage
import com.gym4every1.routes.transition_routes.TransitionScreen2
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.delay

@Composable
fun SecondNavigation(
    navController: NavHostController,
    supabaseClient: SupabaseClient,
    context: Context
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var showSavedVibes by remember { mutableStateOf(false) } // Track saved vibes visibility

    Scaffold(
        topBar = {
            if (currentRoute == "feedPage") {
                TopBar(
                    supabaseClient = supabaseClient,
                    showSavedVibes = showSavedVibes,
                    onShowSavedVibesChange = { newState -> showSavedVibes = newState },
                    showDivider = true
                )
            }
        },
        bottomBar = {
            if (currentRoute != "transitionPage2" && currentRoute != "workoutScreen/{workoutName}/{imageUrl}"
                && currentRoute!= "exerciseDetails/{exerciseId}" && currentRoute!= "exerciseTimer/{programName}"
                && currentRoute!= "detail/sleep" && currentRoute!= "detail/water" && currentRoute!= "detail/nutrition"
                && currentRoute!= "meal_details/{mealType}"){
                BottomNavigationBar(navController)
            }
        },
        content = { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "transitionPage2"
            ) {
                composable("transitionPage2") { TransitionScreen2(navController, context) }
                composable("feedPage") {
                    FeedScreen(
                        supabaseClient = supabaseClient,
                        paddingValues = paddingValues,
                        showSavedVibes = showSavedVibes // Pass state here
                    )
                }
                composable("explorePage") { ExploreScreen(navController,supabaseClient, paddingValues) }
                composable("statsPage") { StatsScreen(navController, supabaseClient, paddingValues) }
                composable("detail/sleep") { SleepTrackingPage(navController, supabaseClient, paddingValues) }
                composable("detail/water") { WaterTrackingPage(navController, supabaseClient, paddingValues) }
                composable("detail/nutrition") { NutritionTrackingPage(navController) }
                composable(
                    "meal_details/{mealType}",
                    arguments = listOf(navArgument("mealType") { type = NavType.StringType })
                ) { backStackEntry ->
                    MealDetailsScreen(
                        mealType = backStackEntry.arguments?.getString("mealType") ?: "",
                    )
                }
                composable(
                    "workoutScreen/{workoutName}/{imageUrl}",
                    arguments = listOf(
                        navArgument("workoutName") { type = NavType.StringType },
                        navArgument("imageUrl") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val workoutName = backStackEntry.arguments?.getString("workoutName") ?: "Cardio"
                    val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
                    WorkoutScreen(navController, supabaseClient, paddingValues, programName = workoutName, thumbnailUrl = imageUrl)
                }
                composable("exerciseDetails/{exerciseId}") { backStackEntry ->
                    val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: return@composable
                    ExerciseDetailsScreen(paddingValues, exerciseId)
                }
                composable("exerciseTimer/{programName}") { backStackEntry ->
                    val programName = backStackEntry.arguments?.getString("programName") ?: return@composable

                    val exercises = remember { mutableStateOf<List<Exercise>?>(null) } // Use nullable list for loading state
                    val isLoading = remember { mutableStateOf(true) }

                    LaunchedEffect(programName) {
                        // Simulate a loading delay (for example, 2 seconds)
                        delay(13000L) // Simulate a 2-second loading delay
                        exercises.value = fetchWorkoutProgram(supabaseClient, programName).map { exercise ->
                            Exercise(
                                name = exercise.name,
                                duration = exercise.duration,
                                breakDuration = exercise.breakDuration
                            )
                        }
                        isLoading.value = false
                    }

                    if (isLoading.value || exercises.value == null) {
                        // Show loading screen with delay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White), // Optional: Add a background color
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator() // Loading spinner
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Loading exercises...", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    } else {
                        // Render the timer screen once data is loaded
                        ExerciseTimerScreen(
                            paddingValues,
                            exercises = exercises.value!!,
                            onWorkoutComplete = { navController.popBackStack() }
                        )
                    }
                }
                composable("profilePage") { ProfileScreen(navController, supabaseClient, paddingValues) }
            }
        }
    )
}