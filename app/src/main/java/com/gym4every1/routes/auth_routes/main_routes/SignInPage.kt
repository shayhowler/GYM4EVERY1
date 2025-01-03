package com.gym4every1.routes.auth_routes.main_routes

import android.content.Context
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.auth.loginUser
import com.gym4every1.database.checkAndNavigate
import com.gym4every1.routes.auth_routes.shared.CustomTextFieldWithIcon
import com.gym4every1.routes.auth_routes.shared.RectBgButton
import com.gym4every1.routes.auth_routes.shared.isValidEmail
import com.gym4every1.routes.auth_routes.shared.validateFields
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(
    navController: NavController,
    supabaseClient: SupabaseClient,
    context: Context,
    signUpViewModel: SignUpViewModel
) {
    LaunchedEffect(Unit) {
        signUpViewModel.clear()
    }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val topPadding = (screenHeight * 0.52f)

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.mipmap.signin), // Set the new image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust image to fill the screen
        )

        // Form Fields for Sign-In
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = topPadding)
        ) {
            CustomTextFieldWithIcon(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email Address",
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Envelope,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 22.dp),
                        tint = if (!isValidEmail(email)) Color.Red else Color(0xFFED4747)
                    )
                },
                isError = !isValidEmail(email),
                modifier = Modifier.width(300.dp)
            )

            CustomTextFieldWithIcon(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Lock,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 22.dp),
                        tint = if (isEmpty(password)) Color.Red else Color(0xFFED4747)
                    )
                },
                isPassword = true,
                isError = isEmpty(password),
                modifier = Modifier.width(300.dp)
            )

            // Forgot Password Link
            Text(
                text = "Forgot Password?",
                color = Color(0xFFED4747),
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    navController.navigate("forgotPassword")
                }
            )

            // Error Message Display
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            // Loading Indicator
            if (isLoading) {
                CircularProgressIndicator()
            }

            // Sign In Button
            RectBgButton(
                onClick = {
                    val error = validateFields(
                        fullName = "",
                        email = email,
                        password = password,
                        confirmPassword = "",
                        isSignIn = true
                    )
                    if (error.isNotEmpty()) {
                        errorMessage = error
                        return@RectBgButton
                    }
                    isLoading = true
                    errorMessage = ""

                    CoroutineScope(Dispatchers.Main).launch {
                        try {
                            loginUser(supabaseClient, email, password)

                            isLoading = false
                            Toast.makeText(context, "Sign-In successful!", Toast.LENGTH_SHORT).show()

                            checkAndNavigate(navController, supabaseClient, email, context)

                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = e.message ?: "Login failed"
                            Log.e("SignIn", "Error: $errorMessage")
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                buttonText = "Sign In",
                modifier = Modifier
                    .width(327.dp)
                    .height(60.dp)
            )

            // Footer with Sign-Up and Back to Menu Links
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 14.dp, bottom = 14.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Don't have an account yet?", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Sign Up",
                        color = Color(0xFFED4747),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            navController.navigate("signUp1")
                        }
                    )
                }

                Text(
                    text = "Back to Menu",
                    textAlign = TextAlign.Center,
                    color = Color(0xFFED4747),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { navController.navigate("authHome") }
                )
            }
        }
    }
}