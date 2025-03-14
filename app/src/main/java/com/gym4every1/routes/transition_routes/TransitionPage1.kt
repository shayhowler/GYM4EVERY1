package com.gym4every1.routes.transition_routes

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gym4every1.R
import com.gym4every1.database.checkAndNavigate
import com.gym4every1.singletons.SessionManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay

@Composable
fun TransitionScreen1(navController: NavController, supabaseClient: SupabaseClient, context: Context) {

    BackHandler {
        Toast.makeText(context, "You can't go back!", Toast.LENGTH_SHORT).show()
    }
    // Simulate transition delay
    LaunchedEffect(Unit) {
        // Wait for a few seconds
        delay(1500)
        if (SessionManager.isAuthenticated(context)) {
            val email = supabaseClient.auth.currentUserOrNull()?.email
            checkAndNavigate(navController, supabaseClient, email.toString(), context)
        }
        else {
            navController.navigate("authHome")
        }

        }

    // Simple loading view with a circular progress indicator
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo_round), // Use your logo resource
            contentDescription = "App Logo",
            modifier = Modifier.size(80.dp) // Adjust size as needed
        )

        CircularProgressIndicator(color = Color.Black, strokeWidth = 5.dp)
    }
}