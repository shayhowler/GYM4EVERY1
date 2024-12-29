package com.gym4every1.routes.navigation_handling

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.gym4every1.routes.auth_routes.main_routes.AuthHomeScreen
import com.gym4every1.routes.auth_routes.main_routes.PasswordResetScreen
import com.gym4every1.routes.auth_routes.main_routes.SignInScreen
import com.gym4every1.routes.auth_routes.main_routes.SignUp1Screen
import com.gym4every1.routes.auth_routes.main_routes.SignUp2Screen
import com.gym4every1.routes.transition_routes.TransitionScreen1
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient

fun authRoutesNav(
    builder: NavGraphBuilder,
    navController: NavHostController,
    supabaseClient: SupabaseClient,
    context: Context,
    signUpViewModel: SignUpViewModel
) {
    builder.apply {
        composable("transitionPage1") { TransitionScreen1(navController, supabaseClient, context) }
        composable("authHome") { AuthHomeScreen(navController, supabaseClient, signUpViewModel) }
        composable("forgotPassword") { PasswordResetScreen(navController, supabaseClient, context) }
        composable("signIn") { SignInScreen(navController, supabaseClient, context, signUpViewModel) }
        composable("signUp1") { SignUp1Screen(navController, supabaseClient, signUpViewModel) }
        composable("signUp2/{isGoogleAuth}") { backStackEntry ->
            val isGoogleAuth = backStackEntry.arguments?.getString("isGoogleAuth")?.toBoolean() == true
            SignUp2Screen(navController, supabaseClient, signUpViewModel, isGoogleAuth)
        }
    }
}
