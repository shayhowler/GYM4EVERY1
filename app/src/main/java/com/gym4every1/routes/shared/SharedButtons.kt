package com.gym4every1.routes.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gym4every1.R

@Composable
fun SignInButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(280.dp)
            .height(56.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(32.dp)
            )
            .border(
                width = 3.dp,
                color = Color(0xFFED4747),
                shape = RoundedCornerShape(32.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = "SIGN IN",
            color = Color(0xFFED4747),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.lato))
        )
    }
}

@Composable
fun SignUpButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .width(280.dp)
            .height(56.dp)
            .background(
                color = Color(0xFFED4747),
                shape = RoundedCornerShape(32.dp)
            )
            .clickable { onClick() }
    ) {
        Text(
            text = "SIGN UP",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(Font(R.font.lato))
        )
    }
}

@Composable
fun RectBgButton(onClick: () -> Unit, buttonText: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(0xFF192126),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 24.dp) // Reduced padding for compactness
    ) {
        Text(
            text = buttonText,
            color = Color.White,
            fontSize = 16.sp, // Slightly smaller font size
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(0.dp)  // Remove unnecessary padding around the text
        )
    }
}