package com.gym4every1.routes.start_routes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.singletons.ProfileViewModel

@Composable
fun GoalPageScreen(
    navController: NavController, viewModel: ProfileViewModel
) {
    var selectedGoal by remember { mutableIntStateOf(1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.25f))
            Text(
                text = "Whats Your Fitness Goal",
                fontSize = 26.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.lato_black)),
                modifier = Modifier.padding(top = 40.dp)
            )

            Text(
                text = "This helps us tailor your experience for optimal results.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.lato)),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(0.05f))

            val goalOptions = listOf(
                "Lose Weight (1)",
                "Maintain Weight (2)",
                "Build Muscle (3)",
                "Increase Endurance (4)",
                "Improve Overall Fitness (5)"
            )

            goalOptions.forEachIndexed { index, goal ->
                GoalOption(
                    text = goal,
                    isSelected = selectedGoal == index + 1,
                    onSelect = { selectedGoal = index + 1 }
                )
            }

            Spacer(modifier = Modifier.weight(0.05f))

            RectBgButton(
                onClick = {
                    viewModel.goal = selectedGoal
                    navController.navigate("successPage")
                },
                buttonText = "Done",
                modifier = Modifier
                    .width(327.dp)
                    .height(80.dp)
                    .padding(bottom = 16.dp)
            )
            IconButton(
                onClick = { navController.navigate("activityPage") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                FaIcon(
                    FaIcons.ArrowLeft,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(0.27f))
        }
    }
}

@Composable
fun GoalOption(text: String, isSelected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onSelect() }
            .background(
                if (isSelected) Color(0xFFE57373) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(16.dp)
            ) // Adding a black border
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = Color.Red)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}
