package com.gym4every1

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
import com.gym4every1.singletons.SignUpViewModel
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)
        val signUpViewModel = SignUpViewModel
        val profileViewModel = ProfileViewModel
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            AppNavigation(navController, supabaseClient, context, signUpViewModel, profileViewModel)
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController, supabaseClient: SupabaseClient,
                  context: Context, signUpViewModel: SignUpViewModel,
                  profileViewModel: ProfileViewModel
) {
    NavHost(navController = navController, startDestination = "authHome") {
        composable("authHome") { AuthHomeScreen(navController, supabaseClient, signUpViewModel) }
        composable("forgotPassword") { PasswordResetScreen(navController, supabaseClient, context) }
        composable("signIn") { SignInScreen(navController, supabaseClient, context, signUpViewModel) }
        composable("signUp1") { SignUp1Screen(navController, signUpViewModel) }
        composable("signUp2") { SignUp2Screen(navController, supabaseClient, signUpViewModel) }
        composable("getStarted") { GetStartedScreen(navController,signUpViewModel, profileViewModel) }
        composable("weightPage") { WeightPageScreen(navController, profileViewModel) }
        composable("heightPage") { HeightPageScreen(navController, profileViewModel) }
        composable("birthdayPage") { BirthdayPageScreen(navController, profileViewModel) }
        composable("activityPage") { ActivityPageScreen(navController, profileViewModel) }
        composable("goalPage") { GoalPageScreen(navController, profileViewModel) }
        composable("successPage") { SuccessPageScreen(navController, supabaseClient, profileViewModel) }
        composable("feedPage") { FeedPageScreen(navController, supabaseClient) }
    }
}