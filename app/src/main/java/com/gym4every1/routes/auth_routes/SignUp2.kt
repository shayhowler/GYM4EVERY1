package com.gym4every1.routes.auth_routes

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.auth.registerUser
import com.gym4every1.routes.shared.CustomTextFieldWithIcon
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignUp2Screen(
    navController: NavController,
    supabaseClient: SupabaseClient,
    viewModel: SignUpViewModel,
    isGoogleAuth: Boolean
) {
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.mipmap.signup), // Set the new image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Adjust image to fill the screen
        )

        // Input Fields
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = (LocalConfiguration.current.screenHeightDp * 0.4).dp)
        ) {
            CustomTextFieldWithIcon(
                value = viewModel.securityQuestion,
                onValueChange = { viewModel.securityQuestion = it },
                placeholder = "Security Question",
                icon = {
                    FaIcon(
                        FaIcons.QuestionCircle,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 25.dp),
                        tint = if (viewModel.securityQuestion.isEmpty()) Color.Red else Color(0xFFED4747)
                    )
                }
            )
            CustomTextFieldWithIcon(
                value = viewModel.answer,
                onValueChange = { viewModel.answer = it },
                placeholder = "Answer",
                icon = {
                    FaIcon(
                        FaIcons.Key,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 25.dp),
                        tint = if (viewModel.answer.isEmpty()) Color.Red else Color(0xFFED4747)
                    )
                }
            )
            CustomTextFieldWithIcon(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                placeholder = "Password",
                isPassword = true,
                icon = {
                    FaIcon(
                        FaIcons.Lock,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 25.dp),
                        tint = if (viewModel.password.isEmpty()) Color.Red else Color(0xFFED4747)
                    )
                }
            )
            CustomTextFieldWithIcon(
                value = viewModel.confirmPassword,
                onValueChange = {
                    viewModel.confirmPassword = it
                },
                placeholder = "Confirm Password",
                isPassword = true,
                icon = {
                    FaIcon(
                        FaIcons.Lock,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 25.dp),
                        tint = if (viewModel.confirmPassword.isEmpty()) Color.Red else Color(0xFFED4747)
                    )
                }
            )

            RectBgButton(
                onClick = {
                    if (viewModel.securityQuestion.isNotEmpty() && viewModel.answer.isNotEmpty()) {
                        // Register user logic
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                // Call registerUser, regardless of user existence
                                val isUserUpdated = registerUser(
                                    supabaseClient = supabaseClient,
                                    userEmail = viewModel.email,
                                    userPassword = viewModel.password,
                                    fullName = viewModel.fullName,
                                    username = viewModel.username,
                                    securityQuestion = viewModel.securityQuestion,
                                    answer = viewModel.answer
                                )

                                if (isUserUpdated) {
                                    // If the user existed, navigate to "Get Started" page
                                    Toast.makeText(context, "You must configure your profile now!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("getStarted") // Navigate to "get_started" page
                                } else {
                                    // If the user is new, navigate to auth home
                                    Toast.makeText(context, "Success! Check your inbox to verify your email!", Toast.LENGTH_SHORT).show()
                                    navController.navigate("authHome") // Navigate to "auth_home" page
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "Registration failed: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                    }
                },
                buttonText = "Sign Up",
                modifier = Modifier.width(327.dp).height(60.dp)
            )
            if (!isGoogleAuth) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(top = 0.dp, bottom = 14.dp)
                ) {
                    Text("Already have an account?", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Sign In",
                        color = Color(0xFFED4747),
                        fontSize = 16.sp,
                        modifier = Modifier.clickable {
                            navController.navigate("signIn")
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
            IconButton(onClick = { navController.navigate("signUp1") }) {
                FaIcon(
                    FaIcons.ArrowLeft, // Using the ArrowLeft icon for back
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}