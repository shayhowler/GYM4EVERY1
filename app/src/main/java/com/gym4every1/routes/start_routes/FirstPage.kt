package com.gym4every1.routes.start_routes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gym4every1.R
import com.gym4every1.routes.shared.RectBgButton

class FirstQuestionPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view
        setContent {
            FirstQuestionPageScreen(
                onContinueClick = {
                    val intent = Intent(this, SecondQuestionPageActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun FirstQuestionPageScreen(
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Tell us about yourself",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.nico_moji)),
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            RectBgButton(
                onClick = onContinueClick,
                buttonText = "Continue",
                modifier = Modifier.fillMaxWidth().padding(16.dp).height(60.dp)
            )
        }
    }
}