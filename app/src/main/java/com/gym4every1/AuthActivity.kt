package com.gym4every1

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.gym4every1.routes.navigation_handling.FirstNavigation
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SessionManager
import com.gym4every1.singletons.SignUpViewModel
import com.gym4every1.singletons.SupabaseClientManager

class AuthActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Install splash screen and set exit animation
        installSplashScreen().apply {
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    1.0f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 1000L
                zoomX.doOnEnd {
                    // Once animation is finished, check for authentication and proceed
                    screen.remove()
                }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    1.0f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 1000L
                zoomY.start()
                zoomX.start()
            }
        }

        val supabaseClient = SupabaseClientManager.getSupabaseClient()
        val signUpViewModel = SignUpViewModel
        val profileViewModel = ProfileViewModel

        // Start listening for auth state changes immediately, but don't display UI yet
        setContent {
            val context = LocalContext.current
            // Only show the UI once auth state is checked
            LaunchedEffect(Unit) {
                SessionManager.listenForAuthChanges(context, supabaseClient)
            }

            // Show UI after splash screen animation ends (authentication check has passed)
            FirstNavigation(
                navController = rememberNavController(),
                supabaseClient = supabaseClient,
                context = context,
                signUpViewModel = signUpViewModel,
                profileViewModel = profileViewModel
            )
        }
    }
}