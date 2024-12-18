package com.gym4every1.routes.auth_routes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.gym4every1.routes.shared.CustomTextFieldWithIcon
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.routes.shared.SpecialBox
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri

class NewPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)
        val resetCode = intent?.data?.getQueryParameter("code")

        setContent {
            if (resetCode != null) {
                NewPasswordScreen(
                    supabaseClient = supabaseClient,
                    resetCode = resetCode
                )
            } else {
                Toast.makeText(
                    this,
                    "Invalid reset link.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
    }
}

@Composable
fun NewPasswordScreen(
    supabaseClient: SupabaseClient,
    resetCode: String
) {
    val newPassword = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    var isPasswordMismatch by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val onSubmitNewPassword: (String) -> Unit = { password ->
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Exchange the code for a session
                supabaseClient.auth.exchangeCodeForSession(resetCode)

                // Update the user's password
                supabaseClient.auth.updateUser {
                    this.password = password
                }

                Toast.makeText(
                    context,
                    "Password successfully reset!",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error resetting password: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    SpecialBox(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.reset_password),
            textAlign = TextAlign.Center,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontFamily = FontFamily(Font(R.font.lato_black)),
            modifier = Modifier
                .padding(top = 152.dp)
                .align(Alignment.TopCenter)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 85.dp)
                .offset(y = 150.dp)
        ) {
            CustomTextFieldWithIcon(
                value = newPassword.value,
                onValueChange = { newPassword.value = it },
                placeholder = "New Password",
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Lock,
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .size(40.dp)
                            .padding(start = 22.dp),
                        tint = if (newPassword.value.length < 8) Color(0xFFD32F2F) else Color(0xFFED4747)
                    )
                },
                isPassword = true,
                isError = newPassword.value.length < 8,
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextFieldWithIcon(
                value = confirmPassword.value,
                onValueChange = {
                    confirmPassword.value = it
                    isPasswordMismatch = newPassword.value != confirmPassword.value
                },
                placeholder = "Confirm Password",
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Lock,
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .size(40.dp)
                            .padding(start = 22.dp),
                        tint = if (isPasswordMismatch) Color(0xFFD32F2F) else Color(0xFFED4747)
                    )
                },
                isPassword = true,
                isError = isPasswordMismatch,
                modifier = Modifier.fillMaxWidth()
            )

            RectBgButton(
                onClick = {
                    if (newPassword.value.isNotEmpty() && !isPasswordMismatch) {
                        onSubmitNewPassword(newPassword.value)
                    } else {
                        Toast.makeText(
                            context,
                            "Passwords do not match or are empty.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                buttonText = "Reset Password",
                modifier = Modifier
                    .width(280.dp)
                    .height(60.dp)
            )
        }
    }
}