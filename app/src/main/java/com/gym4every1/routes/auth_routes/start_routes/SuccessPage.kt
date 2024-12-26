package com.gym4every1.routes.auth_routes.start_routes

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.models.auth_models.Profile
import com.gym4every1.models.auth_models.User
import com.gym4every1.singletons.ProfileViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import java.time.format.DateTimeFormatter


@Composable
fun SuccessPageScreen(
    navController: NavController,
    supabaseClient: SupabaseClient,
    profileViewModel: ProfileViewModel
) {
    val context = LocalContext.current
    BackHandler {
        Toast.makeText(context, "You can't go back!", Toast.LENGTH_SHORT).show()
    }
    var animationCompleted by remember { mutableStateOf(false) }

    // Animate the scale factor of the icon
    val scale by animateFloatAsState(
        targetValue = if (animationCompleted) 10f else 0f, // Scale from 0 to 10
        animationSpec = tween(durationMillis = 1500) // Animation duration
    )

    LaunchedEffect(Unit) {
        try {
            // Step 1: Get the current user's identity
            val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id
                ?: throw Exception("User is not authenticated")

            // Step 2: Fetch the existing user from "users" table
            val existingUsers = supabaseClient
                .from("users")
                .select(columns = Columns.list("id, email, username"))
                .decodeList<User>()

            val currentUser = existingUsers.firstOrNull { it.id == userId }
                ?: throw Exception("User not found in 'users' table")

            // Formatter for YYYY-MM-DD format
            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

            // Step 4: Prepare upsert data
            val profileData = Profile(
                id = userId,
                username = currentUser.username
                    ?: throw Exception("Username is null for the current user"),
                height = ProfileViewModel.userHeight.takeIf { it > 0 },
                weight = ProfileViewModel.userWeight.takeIf { it > 0 },
                dateofbirth = ProfileViewModel.userDateOfBirth?.format(dateFormatter), // Format to YYYY-MM-DD
                activity_level = ProfileViewModel.activityLevel.takeIf { it in 1..5 },
                weight_goal = ProfileViewModel.goal.takeIf { it in 1..5 },
            )

            // Step 5: Upsert profile data into "profiles" table
            supabaseClient.from("profiles").upsert(profileData)

            // Show success toast
            Toast.makeText(context, "Profile saved successfully!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            // Handle errors and show error toast
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        animationCompleted = true
        profileViewModel.clear()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(32.dp)
    ) {
        // Green Tick using FontAwesome with scaling animation
        FaIcon(
            FaIcons.CheckCircle,
            modifier = Modifier
                .scale(scale) // Apply scaling directly here
                .align(Alignment.Center),
            tint = Color.Green,
        )
    }

    LaunchedEffect(key1 = scale) {
        if (scale == 10f) { // When animation completes
            navController.navigate("feedPage")
        }
    }
}