package com.gym4every1.routes.auth_routes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gym4every1.R
import com.gym4every1.auth.GoogleSignInButton
import com.gym4every1.routes.shared.SignInButton
import com.gym4every1.routes.shared.SignUpButton
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient

@Composable
fun AuthHomeScreen(
    navController: NavController,
    supabaseClient: SupabaseClient,
    signUpViewModel: SignUpViewModel
) {
    LaunchedEffect(Unit) {
        signUpViewModel.clear()
    }
    Box(
        modifier = Modifier.fillMaxSize()
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
            // Handle SignIn click
            SignInButton(onClick = { navController.navigate("signIn") })
            // Handle SignUp click
            SignUpButton(onClick = { navController.navigate("signUp1") })
            // Google SignIn button
            GoogleSignInButton(navController, supabaseClient)
        }
    }
}