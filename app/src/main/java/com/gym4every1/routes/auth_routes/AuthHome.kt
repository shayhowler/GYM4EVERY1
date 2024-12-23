package com.gym4every1.routes.auth_routes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gym4every1.R
import com.gym4every1.auth.GoogleSignInButton
import com.gym4every1.routes.shared.Routes
import com.gym4every1.routes.shared.SignInButton
import com.gym4every1.routes.shared.SignUpButton

import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient

class AuthHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)
        setContent {
            AuthHomeScreen(
                onSignInClick = {
                    Routes.navigateToSignIn(this)
                },
                onSignUpClick = {
                    Routes.navigateToSignUp1(this)
                },
                supabaseClient = supabaseClient,
            )
        }
    }
}

@Composable
fun AuthHomeScreen(
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    supabaseClient: SupabaseClient,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Background Image from mipmap
        Image(
            painter = painterResource(id = R.mipmap.authhome),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = (LocalConfiguration.current.screenHeightDp * 0.52).dp)
        ) {
            SignInButton(onClick = onSignInClick)
            SignUpButton(onClick = onSignUpClick)
            GoogleSignInButton(supabaseClient)
        }
    }
}