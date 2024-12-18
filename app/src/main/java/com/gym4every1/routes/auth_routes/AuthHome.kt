package com.gym4every1.routes.auth_routes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gym4every1.R
import com.gym4every1.auth.GoogleSignInButton
import com.gym4every1.routes.shared.SignInButton
import com.gym4every1.routes.shared.SignUpButton
import com.gym4every1.routes.shared.SpecialBox
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient

class AuthHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Supabase Client
        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)
        // Set the content view
        setContent {
            AuthHomeScreen(
                onSignInClick = {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivity(intent)
                },
                onSignUpClick = {
                    val intent = Intent(this, SignUp1Activity::class.java)
                    startActivity(intent)
                },
                supabaseClient = supabaseClient
            )
        }
    }
}

@Composable
fun AuthHomeScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    supabaseClient: SupabaseClient
) {
    SpecialBox(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title Text
        Text(
            text = stringResource(R.string.step_into_y),
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.lato_black)),
            modifier = Modifier
                .padding(top = 152.dp)
                .offset(0.dp, 30.dp)
                .align(Alignment.TopCenter)
        )

        // Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(0.dp, 160.dp)
        ) {
            SignInButton(onClick = onSignInClick)
            SignUpButton(onClick = onSignUpClick)
            GoogleSignInButton(supabaseClient)
        }

        // App Logo
        Image(
            painter = painterResource(id = R.mipmap.applogo),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .offset((-15).dp, (-40).dp)
                .align(Alignment.BottomCenter)
                .padding(start = 32.dp, bottom = 32.dp)
        )
    }
}