package com.gym4every1.routes.start_routes

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gym4every1.R
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.singletons.SignUpViewModel

@Composable
fun GetStartedScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel,
    profileViewModel: ProfileViewModel,
) {
    val context = LocalContext.current
    BackHandler {
        Toast.makeText(context, "You can't go back!", Toast.LENGTH_SHORT).show()
    }

    LaunchedEffect(Unit) {
        signUpViewModel.clear()
        profileViewModel.clear()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.getstarted),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = (LocalConfiguration.current.screenHeightDp * 0.7).dp)
        ) {
            // Title Text
            Text(
                text = "Wherever You Are\n Thrive and Go Far",
                fontSize = 26.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.lato_black)),
                modifier = Modifier
            )
            Text(
                text = "Wellness takes time, find your rhyme",
                fontSize = 20.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.lato)),
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(10.dp))
            RectBgButton(
                onClick = {
                    navController.navigate("weightPage")
                },
                buttonText = "Get Started",
                modifier = Modifier
                    .width(360.dp)
                    .height(60.dp)
            )
        }
    }
}