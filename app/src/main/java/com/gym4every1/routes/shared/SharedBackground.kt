package com.gym4every1.routes.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RectBg(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color(0x29FFFFFF),
                shape = RoundedCornerShape(80.dp)
            )
    )
}

@Composable
fun RectBgWhite(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                color = Color.White,
                shape = RoundedCornerShape(80.dp)
            )
    )
}

@Composable
fun SpecialBox(
    modifier: Modifier = Modifier,
    content: @Composable (BoxScope.() -> Unit) // This allows you to pass composables inside the Box
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFED4747)) // Main background color
    ) {
        // Add background elements (rectangles and white box)
        RectBg(modifier = Modifier
            .size(400.dp)
            .offset(x = 200.dp, y = (-10).dp)
            .rotate(23.43f)
            .align(Alignment.TopEnd)
        )
        RectBg(modifier = Modifier
            .size(400.dp)
            .offset(x = 280.dp, y = (-20).dp)
            .rotate(23.43f)
            .align(Alignment.TopEnd)
        )
        RectBg(modifier = Modifier
            .size(400.dp)
            .offset(x = (-300).dp, y = 30.dp)
            .rotate(23.43f)
            .align(Alignment.TopEnd)
        )
        RectBg(modifier = Modifier
            .size(400.dp)
            .offset(x = (-360).dp, y = 60.dp)
            .rotate(23.43f)
            .align(Alignment.TopEnd)
        )
        RectBg(modifier = Modifier
            .size(400.dp)
            .offset(x = (400).dp, y = 555.dp)
            .rotate(23.43f)
            .align(Alignment.TopEnd)
        )
        RectBg(modifier = Modifier
            .size(400.dp)
            .offset(x = (420).dp, y = 540.dp)
            .rotate(23.43f)
            .align(Alignment.TopEnd)
        )
        RectBgWhite(modifier = Modifier
            .size(width = 700.dp, height = 700.dp)
            .offset(x = (-60).dp, y = 210.dp)
            .rotate(23.43f)
            .align(Alignment.BottomEnd)
        )

        // Insert the content that is passed into the Box
        content()
    }
}