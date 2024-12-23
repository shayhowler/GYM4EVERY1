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
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.routes.shared.Routes
import com.gym4every1.singletons.ProfileViewModel

class ActivityPageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val profileViewModel = ProfileViewModel

        setContent {
            ActivityPageScreen(
                onNavigate = { navigateTo ->
                    when (navigateTo) {
                        "pageBirthday" -> Routes.navigateToBirthdayPage(this)
                        "pageGoal" -> Routes.navigateToGoalPage(this)
                    }
                },
                viewModel = profileViewModel
            )
        }
    }
}

@Composable
fun ActivityPageScreen(
    onNavigate: (String) -> Unit, viewModel: ProfileViewModel
) {
    var selectedActivity by remember { mutableIntStateOf(1) }

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
            Spacer(modifier = Modifier.weight(0.1f))

            Text(
                text = "Whats Your Daily Activity Level",
                fontSize = 26.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.lato_black)),
                modifier = Modifier.padding(top = 40.dp)
            )

            Text(
                text = "This is used to set up and calculate your recommended daily consumption.",
                fontSize = 16.sp,
                color = Color.Gray,
                fontFamily = FontFamily(Font(R.font.lato)),
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val activityLevels = listOf(
                "Sedentary (I do not move or move very little.)",
                "Lightly active (Lightly active lifestyle, I exercise 1-3 days a week).",
                "Moderately active (I lead an active lifestyle / I exercise 3-5 days a week).",
                "Very active (I lead a very active lifestyle / I exercise 6-7 days a week).",
                "Extremely active (Professional athlete or athletic level)."
            )

            activityLevels.forEachIndexed { index, activity ->
                ActivityOption(
                    text = activity,
                    isSelected = selectedActivity == index + 1,
                    onSelect = { selectedActivity = index + 1 }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            RectBgButton(
                onClick = {
                    viewModel.activityLevel = selectedActivity
                    onNavigate("pageGoal")
                },
                buttonText = "Continue",
                modifier = Modifier
                    .width(327.dp)
                    .height(80.dp)
                    .padding(bottom = 16.dp)
            )
            IconButton(
                onClick = { onNavigate("pageBirthday") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                FaIcon(
                    FaIcons.ArrowLeft,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.weight(0.2f))
        }
    }
}

@Composable
fun ActivityOption(text: String, isSelected: Boolean, onSelect: () -> Unit) {
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
