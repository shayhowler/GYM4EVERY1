package com.gym4every1.routes.navigation_handling

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gym4every1.routes.auth_routes.start_routes.ActivityPageScreen
import com.gym4every1.routes.auth_routes.start_routes.BirthdayPageScreen
import com.gym4every1.routes.auth_routes.start_routes.GetStartedScreen
import com.gym4every1.routes.auth_routes.start_routes.GoalPageScreen
import com.gym4every1.routes.auth_routes.start_routes.HeightPageScreen
import com.gym4every1.routes.auth_routes.start_routes.SuccessPageScreen
import com.gym4every1.routes.auth_routes.start_routes.WeightPageScreen
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient

fun getStartedRoutesNav(
    builder: NavGraphBuilder,
    navController: NavHostController,
    supabaseClient: SupabaseClient,
    profileViewModel: ProfileViewModel,
    signUpViewModel: SignUpViewModel,
) {
    builder.apply {
        composable("getStarted") { GetStartedScreen(navController, signUpViewModel, profileViewModel) }
        composable("weightPage") { WeightPageScreen(navController, profileViewModel) }
        composable("heightPage") { HeightPageScreen(navController, profileViewModel) }
        composable("birthdayPage") { BirthdayPageScreen(navController, profileViewModel) }
        composable("activityPage") { ActivityPageScreen(navController, profileViewModel) }
        composable("goalPage") { GoalPageScreen(navController, profileViewModel) }
        composable("successPage") { SuccessPageScreen(navController, supabaseClient, profileViewModel) }
    }
}
