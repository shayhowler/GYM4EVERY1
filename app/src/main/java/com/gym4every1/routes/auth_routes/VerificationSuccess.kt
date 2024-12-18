package com.gym4every1.routes.auth_routes


import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

class VerificationSuccessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VerificationSuccessScreen(onAnimationComplete = {
            })
        }
    }
}

@Composable
fun VerificationSuccessScreen(onAnimationComplete: () -> Unit) {
    var animationCompleted by remember { mutableStateOf(false) }

    // Animate the scale factor of the icon
    val scale by animateFloatAsState(
        targetValue = if (animationCompleted) 10f else 0f, // Scale from 0 to 10
        animationSpec = tween(durationMillis = 1500) // Animation duration
    )

    // Trigger the animation completion
    LaunchedEffect(key1 = scale) {
        if (scale == 10f) { // When animation completes
            onAnimationComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
            .padding(32.dp)
    ) {
        // Green Tick using FontAwesome with scaling animation
        FaIcon(
            FaIcons.CheckCircle,
            modifier = Modifier
                .scale(scale) // Apply scaling directly here
                .align(Alignment.Center),
            tint = Color.Green,
        )
    }

    val context = LocalContext.current
    LaunchedEffect(true) {
        animationCompleted = true
        Toast.makeText(context, "Verification successful!", Toast.LENGTH_SHORT).show()
    }
}