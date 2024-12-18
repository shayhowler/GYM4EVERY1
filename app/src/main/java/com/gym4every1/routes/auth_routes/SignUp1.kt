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
import com.gym4every1.routes.shared.isValidEmail
import com.gym4every1.singletons.SignUpViewModel

class SignUp1Activity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Directly access the SignUpViewModel singleton
        val signUpViewModel = SignUpViewModel

        setContent {
            SignUp1Screen(
                onNavigate = { navigateTo ->
                    when (navigateTo) {
                        "sign_in" -> navigateToSignIn()
                        "sign_up2" -> navigateToSignUp2()
                        "auth_home" -> navigateToAuthHome()
                    }
                },
                viewModel = signUpViewModel
            )
        }
    }

    private fun navigateToSignIn() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToSignUp2() {
        val intent = Intent(this, SignUp2Activity::class.java)
        startActivity(intent)
    }

    private fun navigateToAuthHome() {
        val intent = Intent(this, AuthHomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun SignUp1Screen(onNavigate: (String) -> Unit, viewModel: SignUpViewModel) {
    val context = LocalContext.current

    SpecialBox {
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
                .offset(y = 220.dp)
        ) {
            CustomTextFieldWithIcon(
                value = viewModel.fullName,
                onValueChange = { viewModel.fullName = it },
                placeholder = "Full Name",
                icon = {
                    FaIcon(
                        FaIcons.User,
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .size(40.dp)
                            .padding(start = 25.dp),
                        tint = if (viewModel.fullName.isEmpty()) Color.Red else Color(0xFFED4747)
                    )
                },
                modifier = Modifier.width(300.dp)
            )
            CustomTextFieldWithIcon(
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                placeholder = "Email",
                icon = {
                    FaIcon(
                        FaIcons.Envelope,
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .size(40.dp)
                            .padding(start = 25.dp),
                        tint = if (!isValidEmail(viewModel.email)) Color.Red else Color(0xFFED4747)
                    )
                },
                modifier = Modifier.width(300.dp)
            )

            RectBgButton(
                onClick = {
                    if (viewModel.fullName.isNotEmpty() && isValidEmail(viewModel.email)) {
                        onNavigate("sign_up2")
                    } else {
                        Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                    }
                },
                buttonText = "Continue",
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
            IconButton(onClick = {
                if (viewModel.fullName.isNotEmpty() && isValidEmail(viewModel.email)) {
                    onNavigate("sign_up2")
                } else {
                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                }
            }) {
                FaIcon(
                    FaIcons.ArrowRight,
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    }
}