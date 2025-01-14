package com.gym4every1.routes.app_routes.explore

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun ExploreScreen(navController: NavController, paddingValues: PaddingValues) {
    val context = LocalContext.current
    BackHandler {
        Toast.makeText(context, "You can't go back!", Toast.LENGTH_SHORT).show()
    }

    val popularWorkouts = listOf("Cardio", "Arm Training", "Lower Body Training")
    val otherWorkouts = listOf("Back Training", "Chest Training", "Core Training")
    val workoutImages = mapOf(
        "Cardio" to "https://images.pexels.com/photos/936094/pexels-photo-936094.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
        "Arm Training" to "https://images.pexels.com/photos/5327466/pexels-photo-5327466.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
        "Lower Body Training" to "https://images.pexels.com/photos/20379157/pexels-photo-20379157/free-photo-of-muscular-man-lifting-weight-at-gym.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
        "Back Training" to "https://images.pexels.com/photos/2092479/pexels-photo-2092479.jpeg?auto=compress&cs=tinysrgb&w=800",
        "Chest Training" to "https://images.pexels.com/photos/18060023/pexels-photo-18060023/free-photo-of-bodybuilder-working-out-on-weight-bench.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2",
        "Core Training" to "https://images.pexels.com/photos/3076516/pexels-photo-3076516.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
    )
    val exercises = remember { getShuffledRandomExercises() } // List of Exercise objects
    val exerciseGroups = exercises.map { it.name }.chunked(4) // Group exercise names into rows of 4
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        // Title: Popular Workouts
        Text(
            text = "Popular Workouts",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )
        // Horizontal Pager for Popular Workouts
        val workoutPagerState = rememberPagerState { popularWorkouts.size }
        HorizontalPager(
            state = workoutPagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) { page ->
            val workoutName = popularWorkouts[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // Navigate to WorkoutScreen with the selected workout name and image URL.
                        navController.navigate("workoutScreen/${workoutName}/${Uri.encode(workoutImages[workoutName]!!)}")
                    },
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(workoutImages[workoutName])
                            .crossfade(true) // Optional: Adds a crossfade animation when loading images.
                            .build(),
                        contentDescription = workoutName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds
                    )
                    Text(
                        text = workoutName,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White, // Change text color for better visibility against images.
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        PagerIndicator(
            currentPageIndex = workoutPagerState.currentPage,
            pageCount = popularWorkouts.size
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Pager for Other Workouts
        val otherWorkoutPagerState = rememberPagerState { otherWorkouts.size }
        HorizontalPager(
            state = otherWorkoutPagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) { page ->
            val workoutName = otherWorkouts[page]
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable {
                        // Navigate to WorkoutScreen with the selected workout name and image URL.
                        navController.navigate("workoutScreen/${workoutName}/${Uri.encode(workoutImages[workoutName]!!)}")
                    },
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(workoutImages[workoutName])
                            .crossfade(true)
                            .build(),
                        contentDescription = workoutName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                    Text(
                        text = workoutName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        PagerIndicator(
            currentPageIndex = otherWorkoutPagerState.currentPage,
            pageCount = otherWorkouts.size
        )

// Title: Best for You
        Text(
            text = "Best for You",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(start = 16.dp)
        )

        // Horizontal Pager for Exercise Groups
        val exercisePagerState = rememberPagerState { exerciseGroups.size }
        HorizontalPager(
            state = exercisePagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    exerciseGroups[page].forEachIndexed { index, exercise ->
                        if (index < 2) { // First row with two exercises
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        // Navigate to WorkoutScreen with the selected exercise name
                                        navController.navigate("exerciseDetails/${Uri.encode(exercise)}")
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val imageUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/refs/heads/main/exercises/${exercise}/0.jpg"
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Text(
                                        text = exercise.replace('_', ' '),
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    exerciseGroups[page].forEachIndexed { index, exercise ->
                        if (index >= 2) { // Second row with two exercises
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .clickable {
                                        // Navigate to WorkoutScreen with the selected exercise name
                                        navController.navigate("exerciseDetails/${Uri.encode(exercise)}")
                                    },
                                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    val imageUrl = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/refs/heads/main/exercises/${exercise}/1.jpg"
                                    AsyncImage(
                                        model = imageUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                    Text(
                                        text = exercise.replace('_', ' '),
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.align(Alignment.BottomCenter)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        PagerIndicator(
            currentPageIndex = exercisePagerState.currentPage,
            pageCount = exerciseGroups.size
        )
    }
}

@Composable
fun PagerIndicator(currentPageIndex: Int, pageCount: Int) {
    Row(
        modifier=
            Modifier.wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom=
                    8.dp),
        horizontalArrangement=
            Arrangement.Center) {

        repeat(pageCount) { iteration ->
            val color=
                if (currentPageIndex == iteration) Color.DarkGray else Color.LightGray

            Box(
                modifier=
                    Modifier.padding(2.dp)
                        .size(8.dp)
                        .background(color, shape=
                            RoundedCornerShape(8.dp))
            )
        }
    }
}