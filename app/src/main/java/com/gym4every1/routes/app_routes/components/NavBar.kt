package com.gym4every1.routes.app_routes.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        Triple(FaIcons.Home, "Home", "feedPage"),
        Triple(FaIcons.Rocket, "Explore", "explorePage"),
        Triple(FaIcons.ChartBarRegular, "Stats", "statsPage"),
        Triple(FaIcons.User, "Profile", "profilePage")
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF192126),
                shape = RectangleShape
            )
            .padding(horizontal = 40.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { (icon, label, route) ->
            val isSelected = currentRoute == route

            // Smoothly animate the width of the red box
            val boxWidth by animateDpAsState(
                targetValue = if (isSelected) 120.dp else 50.dp,
                animationSpec = tween(durationMillis = 300)
            )
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) Color(0xFFED4747) else Color.Transparent,
                animationSpec = tween(durationMillis = 300)
            )

            Box(
                modifier = Modifier
                    .clickable {
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                    .background(
                        color = backgroundColor,
                        shape = CircleShape
                    )
                    .size(height = 50.dp, width = boxWidth) // Height fixed, width animated
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    FaIcon(
                        icon,
                        tint = if (isSelected) Color(0xFF192126) else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            color = Color(0xFF192126),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}