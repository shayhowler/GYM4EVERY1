package com.gym4every1

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.gym4every1.routes.AppNavigation
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SessionManager
import com.gym4every1.singletons.SignUpViewModel
import com.gym4every1.singletons.SupabaseClientManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

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
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    1.0f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 1000L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }

        super.onCreate(savedInstanceState)

        val supabaseClient = SupabaseClientManager.getSupabaseClient()
        val signUpViewModel = SignUpViewModel
        val profileViewModel = ProfileViewModel

        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val startDestination = remember { getStartDestination(context) }

            // Start listening to auth changes
            LaunchedEffect(Unit) {
                SessionManager.listenForAuthChanges(context, supabaseClient)
            }

            AppNavigation(navController, supabaseClient, context,
                signUpViewModel, profileViewModel, startDestination)

        }
    }

    private fun getStartDestination(context: Context): String {
        return if (SessionManager.isAuthenticated(context)) {
            "feedPage"
        } else {
            "authHome"
        }
    }
}