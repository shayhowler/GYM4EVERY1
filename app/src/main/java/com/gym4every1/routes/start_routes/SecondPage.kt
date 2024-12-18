package com.gym4every1.routes.start_routes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gym4every1.R
import com.gym4every1.routes.shared.RectBgButton

class SecondQuestionPageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view
        setContent {
            SecondQuestionPageScreen(
                onContinueClick = {
                    val intent = Intent(this, ThirdQuestionPageActivity::class.java)
                    startActivity(intent)
                }
            )
        }
    }
}

@Composable
fun SecondQuestionPageScreen(
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        // Title Text
        Text(
            text = "Wherever You Are\n Thrive and Go Far",
            fontSize = 26.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato_black)),
            modifier = Modifier
                .padding(top = 152.dp)
                .offset(y = 455.dp)
                .align(Alignment.TopCenter)
        )

        Text(
            text = "Wellness takes time, find your rhyme",
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = FontFamily(Font(R.font.lato)),
            modifier = Modifier
                .padding(top = 152.dp)
                .offset(y = 550.dp)
                .align(Alignment.TopCenter)
        )

        RectBgButton(
            onClick = onContinueClick,
            buttonText = "Continue",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
                .offset(y = (-40).dp)
                .align(Alignment.BottomCenter)
        )
    }
}