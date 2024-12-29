package com.gym4every1.routes.auth_routes.start_routes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.routes.auth_routes.shared.RectBgButton
import com.gym4every1.singletons.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun WeightPageScreen(
    navController: NavController, viewModel: ProfileViewModel
) {
    val minWeight = 30
    val maxWeight = 230
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = viewModel.userWeight - minWeight)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp), // Ensures spacing between components
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.45f))
        // Title and Subtitle Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "How Much Do You Weigh?",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(resId = R.font.lato_black)),
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "This is used to set up and calculate your recommended daily consumption.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        // Weight Display Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${viewModel.userWeight} kg",
                fontSize = 36.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(resId = R.font.lato_black)),
            )
            Text(
                text = "${(viewModel.userWeight * 2.205).toInt()} lbs",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(resId = R.font.lato_black)),
                color = Color.Gray
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        // Weight Selector Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            LazyRow(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(horizontal = 33.dp)
            ) {
                items((maxWeight - minWeight + 1)) { index ->
                    val weightValue = minWeight + index
                    val isMajorTick = weightValue % 5 == 0

                    // Draw tick marks
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(if (isMajorTick) 80.dp else 50.dp)
                            .background(Color.LightGray)
                    )

                    // Space between tick marks
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            // Center Red Line (Selection Indicator)
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .height(80.dp)
                    .width(2.dp)
                    .background(Color.Red)
            )
        }

        // Update selected weight as the user scrolls.
        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .collectLatest { index ->
                    val weightValue = minWeight + index
                    viewModel.userWeight = weightValue.coerceIn(minWeight, maxWeight)
                }
        }

        // Continue Button Section
        RectBgButton(
            onClick = {
                    navController.navigate("heightPage")
            },
            buttonText = "Continue",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
        )
        IconButton(onClick = { navController.navigate("selectionPage") }) {
            FaIcon(
                FaIcons.ArrowLeft, // Using the ArrowLeft icon for back
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.weight(0.8f))
    }
}