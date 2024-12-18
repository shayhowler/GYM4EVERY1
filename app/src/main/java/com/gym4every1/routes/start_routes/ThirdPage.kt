package com.gym4every1.routes.start_routes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gym4every1.R
import com.gym4every1.auth.logoutUser
import com.gym4every1.routes.auth_routes.AuthHomeActivity
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThirdQuestionPageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Supabase client
        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)

        // Set the content view
        setContent {
            ThirdQuestionPageScreen(
                supabaseClient = supabaseClient,
                onGetStartedClick = {
                    // Handle the navigation logic after logout
                    val intent = Intent(this, AuthHomeActivity::class.java)
                    startActivity(intent)
                    finish()  // Finish the current activity to remove it from the back stack
                }
            )
        }
    }
}

@Composable
fun ThirdQuestionPageScreen(
    supabaseClient: SupabaseClient,
    onGetStartedClick: () -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Title Text
        Text(
            text = "Wherever You Are\n Thrive and Go Far",
            fontSize = 26.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato_black)),
            modifier = Modifier
                .padding(top = 152.dp)
                .offset(y = 455.dp)
                .align(Alignment.TopCenter)
        )

        Text(
            text = "Wellness takes time, find your rhyme",
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato)),
            modifier = Modifier
                .padding(top = 152.dp)
                .offset(y = 550.dp)
                .align(Alignment.TopCenter)
        )

        RectBgButton(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        // Call the logout function using Supabase
                        logoutUser(supabaseClient)
                        Toast.makeText(context, "You have been logged out.", Toast.LENGTH_SHORT).show()

                        // Handle navigation to AuthHomeActivity after logout
                        onGetStartedClick()
                    } catch (e: Exception) {
                        // Handle logout errors
                        Log.e("LogoutError", "Error: ${e.message}")
                        Toast.makeText(context, "Failed to log out: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            buttonText = "Get Started",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
                .offset(y = (-40).dp)
                .align(Alignment.BottomCenter)
        )
    }
}