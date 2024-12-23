package com.gym4every1.routes.start_routes

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.singletons.ProfileViewModel
import java.time.LocalDate
import java.util.Calendar

@Composable
fun BirthdayPageScreen(
    navController: NavController,
    viewModel: ProfileViewModel
) {
    var selectedDate by remember { mutableStateOf(viewModel.userDateOfBirth ?: LocalDate.now()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.3f))
        // Title and Subtitle
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "When Were You Born?",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.lato_black)),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "We use your birthday to personalize your experience.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // Birthday Selection Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
            elevation = CardDefaults.elevatedCardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Selected Date",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = selectedDate.toString(),
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.lato_black)),
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                selectedDate = LocalDate.of(year, month + 1, day)
                                viewModel.userDateOfBirth = selectedDate.toString()
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        datePickerDialog.show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFED4747)),
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(text = "Pick a Date", color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.2f))

        // Continue Button
        RectBgButton(
            onClick = { navController.navigate("activityPage") },
            buttonText = "Continue",
            modifier = Modifier
                .width(327.dp)
                .height(80.dp)
                .padding(bottom = 16.dp)
        )
        // Top Back Button
        IconButton(
            onClick = { navController.navigate("heightPage") },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            FaIcon(
                FaIcons.ArrowLeft,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.weight(0.25f))
    }
}