package com.gym4every1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.gym4every1.singletons.SessionManager
import com.gym4every1.singletons.SupabaseClientManager
import com.gym4every1.routes.navigation_handling.SecondNavigation

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val supabaseClient = SupabaseClientManager.getSupabaseClient()
            val navController = rememberNavController()
            val context = LocalContext.current

            // Start listening to auth changes
            LaunchedEffect(Unit) {
                SessionManager.listenForAuthChanges(context, supabaseClient)
            }

            // SecondNavigation now includes Scaffold, so we just call it
            SecondNavigation(navController, supabaseClient, context = context)
        }
    }
}