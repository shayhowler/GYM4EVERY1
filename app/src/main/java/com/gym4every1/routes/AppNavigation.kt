package com.gym4every1.routes

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gym4every1.routes.auth_routes.AuthHomeScreen
import com.gym4every1.routes.auth_routes.PasswordResetScreen
import com.gym4every1.routes.auth_routes.SignInScreen
import com.gym4every1.routes.auth_routes.SignUp1Screen
import com.gym4every1.routes.auth_routes.SignUp2Screen
import com.gym4every1.routes.feed.FeedPageScreen
import com.gym4every1.routes.start_routes.ActivityPageScreen
import com.gym4every1.routes.start_routes.BirthdayPageScreen
import com.gym4every1.routes.start_routes.GetStartedScreen
import com.gym4every1.routes.start_routes.GoalPageScreen
import com.gym4every1.routes.start_routes.HeightPageScreen
import com.gym4every1.routes.start_routes.SuccessPageScreen
import com.gym4every1.routes.start_routes.WeightPageScreen
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SessionManager
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient

@Composable
fun AppNavigation(
    navController: NavHostController,
    supabaseClient: SupabaseClient,
    context: Context,
    signUpViewModel: SignUpViewModel,
    profileViewModel: ProfileViewModel,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("authHome") { AuthHomeScreen(navController, supabaseClient, signUpViewModel) }
        composable("forgotPassword") { PasswordResetScreen(navController, supabaseClient, context) }
        composable("signIn") { SignInScreen(navController, supabaseClient, context, signUpViewModel) }
        composable("signUp1") { SignUp1Screen(navController, supabaseClient, signUpViewModel) }
        composable("signUp2/{isGoogleAuth}") { backStackEntry ->
            val isGoogleAuth = backStackEntry.arguments?.getString("isGoogleAuth")?.toBoolean() == true
            SignUp2Screen(navController, supabaseClient, signUpViewModel, isGoogleAuth)
        }
        composable("getStarted") {
            if (SessionManager.isAuthenticated(context)) {
                GetStartedScreen(navController, supabaseClient, signUpViewModel, profileViewModel)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("weightPage/{isGoogleAuth}") { backStackEntry ->
            val isGoogleAuth = backStackEntry.arguments?.getString("isGoogleAuth")?.toBoolean() == true
            if (SessionManager.isAuthenticated(context)) {
                WeightPageScreen(navController, profileViewModel, isGoogleAuth)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("heightPage") {
            if (SessionManager.isAuthenticated(context)) {
                HeightPageScreen(navController, profileViewModel)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("birthdayPage") {
            if (SessionManager.isAuthenticated(context)) {
                BirthdayPageScreen(navController, profileViewModel)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("activityPage") {
            if (SessionManager.isAuthenticated(context)) {
                ActivityPageScreen(navController, profileViewModel)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("goalPage") {
            if (SessionManager.isAuthenticated(context)) {
                GoalPageScreen(navController, profileViewModel)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("successPage") {
            if (SessionManager.isAuthenticated(context)) {
                SuccessPageScreen(navController, supabaseClient, profileViewModel)
            } else {
                navController.navigate("authHome")
            }
        }
        composable("feedPage") {
            if (SessionManager.isAuthenticated(context)) {
                FeedPageScreen(navController, supabaseClient)
            } else {
                navController.navigate("authHome")
            }
        }
    }
}