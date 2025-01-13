package com.gym4every1.routes.navigation_handling

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gym4every1.routes.app_routes.components.BottomNavigationBar
import com.gym4every1.routes.app_routes.components.TopBar
import com.gym4every1.routes.app_routes.explore.ExploreScreen
import com.gym4every1.routes.app_routes.feed.FeedScreen
import com.gym4every1.routes.app_routes.profile.ProfileScreen
import com.gym4every1.routes.app_routes.stats.NutritionTrackingPage
import com.gym4every1.routes.app_routes.stats.SleepTrackingPage
import com.gym4every1.routes.app_routes.stats.StatsScreen
import com.gym4every1.routes.app_routes.stats.WaterTrackingPage
import com.gym4every1.routes.transition_routes.TransitionScreen2
import io.github.jan.supabase.SupabaseClient

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
            if (currentRoute != "transitionPage2") {
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
                composable("explorePage") { ExploreScreen(paddingValues) }
                composable("statsPage") { StatsScreen(navController, supabaseClient, paddingValues) }
                composable("detail/sleep") { SleepTrackingPage(navController, supabaseClient, paddingValues) }
                composable("detail/water") { WaterTrackingPage(navController, supabaseClient, paddingValues) }
                composable("detail/nutrition") { NutritionTrackingPage(navController, paddingValues) }
                composable("profilePage") { ProfileScreen(navController, supabaseClient, paddingValues) }
            }
        }
    )
}