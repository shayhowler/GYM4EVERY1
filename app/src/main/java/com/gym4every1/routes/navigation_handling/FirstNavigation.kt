package com.gym4every1.routes.navigation_handling

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient

@Composable
fun FirstNavigation(
    navController: NavHostController,
    supabaseClient: SupabaseClient,
    context: Context,
    signUpViewModel: SignUpViewModel,
    profileViewModel: ProfileViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = "transitionPage1"
    ) {
        // Authentication routes (No Scaffold)
        authRoutesNav(this, navController, supabaseClient, context, signUpViewModel)
        // Onboarding or Get Started routes (No Scaffold)
        getStartedRoutesNav(this, navController, supabaseClient, profileViewModel, signUpViewModel)
    }
}