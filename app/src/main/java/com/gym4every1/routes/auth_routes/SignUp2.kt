package com.gym4every1.routes.auth_routes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
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
import com.gym4every1.auth.registerUser
import com.gym4every1.routes.shared.CustomTextFieldWithIcon
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.routes.shared.SpecialBox
import com.gym4every1.singletons.SupabaseClientManager
import com.gym4every1.singletons.SignUpViewModel
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUp2Activity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Directly access the SignUpViewModel singleton
        val signUpViewModel = SignUpViewModel

        // Get the SupabaseClient from the singleton
        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)

        setContent {
            SignUp2Screen(
                supabaseClient,
                viewModel = signUpViewModel,
                onNavigate = { navigateTo ->
                    when (navigateTo) {
                        "sign_in" -> navigateToSignIn()
                        "sign_up1" -> navigateToSignUp1()
                        "auth_home" -> navigateToAuthHome()
                    }
                }
            )
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignUp1() {
        val intent = Intent(this, SignUp1Activity::class.java)
        startActivity(intent)
    }

    private fun navigateToAuthHome() {
        val intent = Intent(this, AuthHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun SignUp2Screen(
    supabaseClient: SupabaseClient,
    viewModel: SignUpViewModel,
    onNavigate: (String) -> Unit
) {
    var isPasswordMismatch by remember { mutableStateOf(false) }
    val context = LocalContext.current

    SpecialBox {
        // Title Text
        Text(
            text = stringResource(R.string.create_your_account),
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 85.dp)
                .offset(y = 240.dp)
        ) {
            CustomTextFieldWithIcon(
                value = viewModel.securityQuestion,
                onValueChange = { viewModel.securityQuestion = it },
                placeholder = "Security Question",
                icon = { FaIcon(FaIcons.QuestionCircle,
                    modifier = Modifier
                        .offset(y = 10.dp)
                        .size(40.dp)
                        .padding(start = 25.dp),
                    tint = if (viewModel.securityQuestion.isEmpty()) Color.Red else Color(0xFFED4747)
                ) }
            )
            CustomTextFieldWithIcon(
                value = viewModel.answer,
                onValueChange = { viewModel.answer = it },
                placeholder = "Answer",
                icon = { FaIcon(FaIcons.Key,
                    modifier = Modifier
                        .offset(y = 10.dp)
                        .size(40.dp)
                        .padding(start = 25.dp),
                    tint = if (viewModel.answer.isEmpty()) Color.Red else Color(0xFFED4747)
                    )
                },
            )
            CustomTextFieldWithIcon(
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                placeholder = "Password",
                isPassword = true,
                icon = { FaIcon(FaIcons.Lock,
                    modifier = Modifier
                    .offset(y = 10.dp)
                    .size(40.dp)
                    .padding(start = 25.dp),
                    tint = if (viewModel.password.isEmpty()) Color.Red else Color(0xFFED4747)) }
            )
            CustomTextFieldWithIcon(
                value = viewModel.confirmPassword,
                onValueChange = {
                    viewModel.confirmPassword = it
                    isPasswordMismatch = viewModel.password != viewModel.confirmPassword
                },
                placeholder = "Confirm Password",
                isPassword = true,
                icon = { FaIcon(FaIcons.Lock,modifier = Modifier
                    .offset(y = 10.dp)
                    .size(40.dp)
                    .padding(start = 25.dp),
                    tint = if (viewModel.confirmPassword.isEmpty()) Color.Red else Color(0xFFED4747)
                ) }
            )

            if (isPasswordMismatch) {
                Text(
                    text = "Passwords do not match",
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }

            RectBgButton(
                onClick = {
                    if (viewModel.securityQuestion.isNotEmpty() && viewModel.answer.isNotEmpty() && !isPasswordMismatch) {
                        // Register user logic
                        CoroutineScope(Dispatchers.Main).launch {
                            try {
                                registerUser(
                                    supabaseClient = supabaseClient,
                                    userEmail = viewModel.email,
                                    userPassword = viewModel.password,
                                    fullName = viewModel.fullName,
                                    securityQuestion = viewModel.securityQuestion,
                                    answer = viewModel.answer
                                )
                                Toast.makeText(context, "Success! Check your inbox to verify your email!", Toast.LENGTH_SHORT).show()
                                onNavigate("auth_home")
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
                        onNavigate("sign_in")
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

            IconButton(onClick = { onNavigate("sign_up1") }) {
                FaIcon(
                    FaIcons.ArrowLeft,  // Using the ArrowLeft icon for back
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}