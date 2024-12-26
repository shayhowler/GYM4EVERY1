package com.gym4every1.routes.navigation_handling

import android.content.Context
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.gym4every1.routes.app_routes.components.BottomNavigationBar
import com.gym4every1.routes.app_routes.components.TopBar
import com.gym4every1.routes.app_routes.explore.ExploreScreen
import com.gym4every1.routes.app_routes.feed.FeedScreen
import com.gym4every1.routes.app_routes.profile.ProfileScreen
import com.gym4every1.routes.app_routes.stats.StatsScreen
import com.gym4every1.routes.transition_routes.TransitionScreen2
import io.github.jan.supabase.SupabaseClient

@Composable
fun SecondNavigation(
    navController: NavHostController,
    supabaseClient: SupabaseClient,
    context: Context
) {
    // Get the current route from the navigation back stack
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // Determine the route name
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            // Display TopBar if current route is feedPage
            if (currentRoute == "feedPage") {
                TopBar(true)
            }
        },
        bottomBar = {
            // Display BottomNavigationBar if current route is not transitionPage2
            if (currentRoute != "transitionPage2") {
                BottomNavigationBar(navController)
            }
        },
        content = { paddingValues ->
            // NavHost for managing the navigation and screen transitions
            NavHost(
                navController = navController,
                startDestination = "feedPage"
            ) {
                composable("transitionPage2") { TransitionScreen2(navController, context) }
                composable("feedPage") { FeedScreen(paddingValues) }
                composable("explorePage") { ExploreScreen(paddingValues) }
                composable("statsPage") { StatsScreen(paddingValues) }
                composable("profilePage") { ProfileScreen(navController, supabaseClient, paddingValues) }
            }
        }
    )
}