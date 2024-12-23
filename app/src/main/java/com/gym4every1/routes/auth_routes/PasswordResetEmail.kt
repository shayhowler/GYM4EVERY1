package com.gym4every1.routes.auth_routes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.auth.sendPasswordResetEmail
import com.gym4every1.routes.shared.CustomTextFieldWithIcon
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.routes.shared.isValidEmail
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SupabaseClient here, if needed
        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)
        setContent {
            PasswordResetScreen(supabaseClient, this)
        }
    }
}

@Composable
fun PasswordResetScreen(supabaseClient: SupabaseClient, context: Context) {
    val email = remember { mutableStateOf("") }

    val onEmailSubmitted: (String) -> Unit = { userEmail ->
        CoroutineScope(Dispatchers.Main).launch {
            try {
                sendPasswordResetEmail(
                    supabaseClient = supabaseClient,
                    userEmail = userEmail,
                    onSuccess = {
                        Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, AuthHomeActivity::class.java))
                        (context as ForgotPasswordActivity).finish()
                    },
                    onError = { error ->
                        Toast.makeText(context, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                )
            } catch (e: Exception) {
                Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.mipmap.forgot), // Background image
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Form and Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = (LocalConfiguration.current.screenHeightDp * 0.52).dp)
        ) {
            Text(
                text = "Enter your registered email address.\nWe'll send you a link to reset your password.",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.lato_black)),
                color = Color(0xFFED4747),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
            // Email Input
            CustomTextFieldWithIcon(
                value = email.value,
                onValueChange = { email.value = it },
                placeholder = "Email Address",
                icon = {
                    FaIcon(
                        faIcon = FaIcons.Envelope,
                        modifier = Modifier
                            .offset(y = 12.dp)
                            .size(40.dp)
                            .padding(start = 22.dp),
                        tint = if (isValidEmail(email.value)) Color(0xFFED4747) else Color(0xFFD32F2F)
                    )
                },
                modifier = Modifier.width(350.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Send Reset Link Button
            RectBgButton(
                onClick = {
                    if (isValidEmail(email.value)) {
                        onEmailSubmitted(email.value)
                    } else {
                        Toast.makeText(
                            context,
                            "Please enter a valid email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                buttonText = "Send Reset Link",
                modifier = Modifier
                    .width(280.dp)
                    .height(60.dp)
            )

            // Back to Menu Button
            Text(
                text = "Back to Menu",
                textAlign = TextAlign.Center,
                color = Color(0xFFED4747),
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable {
                        context.startActivity(Intent(context, AuthHomeActivity::class.java))
                        (context as ForgotPasswordActivity).finish()
                    }
            )
        }
    }
}