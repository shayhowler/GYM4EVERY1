package com.gym4every1.routes.auth_routes

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.auth.loginUser
import com.gym4every1.routes.shared.CustomTextFieldWithIcon
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.routes.shared.SpecialBox
import com.gym4every1.routes.shared.isValidEmail
import com.gym4every1.routes.shared.validateFields
import com.gym4every1.routes.start_routes.GetStartedActivity
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)

        setContent {
            SignInScreen(
                supabaseClient = supabaseClient, // Pass supabaseClient here
                onNavigate = { navigateTo ->
                    when (navigateTo) {
                        "get_started" -> navigateToGetStarted()
                        "sign_up" -> navigateToSignUp()
                        "auth_home" -> navigateToAuthHome()
                        "forgot_password" -> navigateToForgotPassword()
                    }
                }
            )
        }
    }

    private fun navigateToGetStarted() {
        // Navigate to the Get Started activity
        val intent = Intent(this, GetStartedActivity::class.java)
        startActivity(intent)
        finish()  // Optionally finish this activity
    }

    private fun navigateToSignUp() {
        // Navigate to the Sign-Up activity
        val intent = Intent(this, SignUp1Activity::class.java)
        startActivity(intent)
        finish()  // Optionally finish this activity
    }

    private fun navigateToAuthHome() {
        // Navigate back to the Auth Home activity
        val intent = Intent(this, AuthHomeActivity::class.java)
        startActivity(intent)
        finish()  // Optionally finish this activity
    }

    private fun navigateToForgotPassword() {
        // Navigate to Forgot Password activity
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
        finish()  // Optionally finish this activity
    }
}

@Composable
fun SignInScreen(
    supabaseClient: SupabaseClient, // Add supabaseClient parameter
    onNavigate: (String) -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    SpecialBox(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title Text
        Text(
            text = stringResource(R.string.sign_in_screen),
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

        // Form Fields for Sign-In (adjusted position)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.align(Alignment.Center)
                .offset(0.dp, 210.dp)
        ) {
            CustomTextFieldWithIcon(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email Address",
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Envelope,
                        modifier = Modifier
                            .offset(y = 10.dp)
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
                            .offset(y = 10.dp)
                            .size(40.dp)
                            .padding(start = 22.dp),
                        tint = if (isEmpty(password)) Color.Red else Color(0xFFED4747)
                    )
                },
                isPassword = true,
                isError = isEmpty(password),
                modifier = Modifier.width(300.dp)
            )

            Text(
                text = "Forgot Password?",
                color = Color(0xFFED4747),
                fontSize = 14.sp,
                modifier = Modifier.clickable {
                    onNavigate("forgot_password")
                }
            )

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

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
                            // Call the loginUser function
                            loginUser(supabaseClient, email, password)

                            // On success
                            isLoading = false
                            Toast.makeText(context, "Sign-In successful!", Toast.LENGTH_SHORT).show()
                            onNavigate("get_started")
                        } catch (e: Exception) {
                            isLoading = false
                            errorMessage = e.message ?: "Login failed"
                            Log.e("SignIn", "Error: $errorMessage")
                        }
                    }
                },
                buttonText = "Sign In",
                modifier = Modifier.width(327.dp).height(60.dp)
            )

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 0.dp, bottom = 14.dp)
            ) {
                Text("Don't have an account yet?", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Sign Up",
                    color = Color(0xFFED4747),
                    fontSize = 16.sp,
                    modifier = Modifier.clickable {
                        onNavigate("sign_up")
                    }
                )
            }

            Text(
                text = "Back to Menu",
                textAlign = TextAlign.Center,
                color = Color(0xFFED4747),
                fontSize = 16.sp,
                modifier = Modifier.clickable { onNavigate("auth_home") }
            )
        }
    }
}