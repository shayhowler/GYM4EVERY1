package com.gym4every1.routes.app_routes.explore

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

@Composable
fun ExploreScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    BackHandler {
        Toast.makeText(context, "You can't go back!", Toast.LENGTH_SHORT).show()
    }

    val popularWorkouts = listOf("Cardio", "Hand Training", "Lower Body Training")
    val otherWorkouts = listOf("Back Training", "Chest Training", "Belly Training")
    val exerciseGroups = (1..12).chunked(4) // Divide exercises into groups of 4

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        // Title: Popular Workouts
        Text(
            text = "Popular Workouts",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(16.dp)
        )

        // Horizontal Pager for Popular Workouts
        val workoutPagerState = rememberPagerState { popularWorkouts.size }
        HorizontalPager(
            state = workoutPagerState,
            modifier = Modifier.fillMaxWidth().height(150.dp) // Reduced height
        ) { page ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = popularWorkouts[page],
                        fontSize = 18.sp, // Uniform font size
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp)) // Space between rows
        // Custom Pager Indicator for Popular Workouts
        PagerIndicator(currentPageIndex = workoutPagerState.currentPage, pageCount = popularWorkouts.size)

        Spacer(modifier = Modifier.height(16.dp))
        val otherWorkoutPagerState = rememberPagerState { otherWorkouts.size }
        HorizontalPager(
            state = otherWorkoutPagerState,
            modifier = Modifier.fillMaxWidth().height(150.dp) // Matching the Popular Workouts height
        ) { page ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor= Color.LightGray),
                shape= RoundedCornerShape(16.dp),
                elevation= CardDefaults.cardElevation(defaultElevation= 8.dp)
            ) {
                Box(
                    modifier= Modifier.fillMaxSize(),
                    contentAlignment= Alignment.Center
                ) {
                    Text(
                        text= otherWorkouts[page],
                        fontSize= 18.sp, // Uniform font size
                        fontWeight= FontWeight.Bold,
                        color= Color.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp)) // Space between rows
        // Custom Pager Indicator for Other Workouts
        PagerIndicator(currentPageIndex= otherWorkoutPagerState.currentPage, pageCount= otherWorkouts.size)

        Spacer(modifier= Modifier.height(16.dp))

        // Title: Best for You
        Text(
            text= "Best for You",
            fontSize= 24.sp,
            fontWeight= FontWeight.Bold,
            color= Color.Black,
            modifier= Modifier.padding(16.dp)
        )

        // Horizontal Pager for Exercise Groups displaying four cards in two rows.
        val exercisePagerState= rememberPagerState { exerciseGroups.size }

        HorizontalPager(
            state= exercisePagerState,
            modifier= Modifier.fillMaxWidth().height(250.dp), // Adjust height to fit the grid layout.
        ) { page ->
            Column(modifier = Modifier.fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    exerciseGroups[page].forEachIndexed { index, exercise ->
                        if (index < 2) { // First row with two exercises.
                            Card(
                                modifier= Modifier
                                    .weight(1f)
                                    .size(100.dp) // Set a smaller size for the card.
                                    .padding(4.dp), // Add padding between cards.
                                colors= CardDefaults.cardColors(containerColor= Color.LightGray),
                                shape= RoundedCornerShape(8.dp),
                                elevation= CardDefaults.cardElevation(defaultElevation= 4.dp)
                            ) {
                                Box(
                                    modifier= Modifier.fillMaxSize(),
                                    contentAlignment= Alignment.Center
                                ) {
                                    Text(
                                        text= "Exercise $exercise",
                                        fontSize= 14.sp,
                                        fontWeight= FontWeight.Bold,
                                        color= Color.Black
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp)) // Space between rows
                Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    exerciseGroups[page].forEachIndexed { index, exercise ->
                        if (index >= 2) { // Second row with two exercises.
                            Card(
                                modifier= Modifier
                                    .weight(1f)
                                    .size(100.dp) // Set a smaller size for the card.
                                    .padding(4.dp), // Add padding between cards.
                                colors= CardDefaults.cardColors(containerColor= Color.LightGray),
                                shape= RoundedCornerShape(8.dp),
                                elevation= CardDefaults.cardElevation(defaultElevation= 4.dp)
                            ) {
                                Box(
                                    modifier= Modifier.fillMaxSize(),
                                    contentAlignment= Alignment.Center
                                ) {
                                    Text(
                                        text= "Exercise $exercise",
                                        fontSize= 14.sp,
                                        fontWeight= FontWeight.Bold,
                                        color= Color.Black
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Custom Pager Indicator for Exercise Groups.
        PagerIndicator(currentPageIndex= exercisePagerState.currentPage, pageCount= exerciseGroups.size)

        Spacer(modifier= Modifier.height(16.dp))
    }
}

@Composable
fun PagerIndicator(currentPageIndex: Int, pageCount: Int) {
    Row(
        modifier= Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(bottom= 8.dp),
        horizontalArrangement= Arrangement.Center
    ) {
        repeat(pageCount) { iteration ->
            val color= if (currentPageIndex == iteration) Color.DarkGray else Color.LightGray

            Box(
                modifier= Modifier
                    .padding(2.dp)
                    .size(8.dp)
                    .background(color, shape= RoundedCornerShape(8.dp))
            )
        }
    }
}