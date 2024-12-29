package com.gym4every1.routes.app_routes.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons

@Composable
fun BottomNavigationBar(navController: NavController) {
    // List of icons and corresponding labels/routes
    val items = listOf(
        Triple(FaIcons.Home, "Home", "feedPage"),
        Triple(FaIcons.Rocket, "Explore", "explorePage"),
        Triple(FaIcons.ChartBarRegular, "Stats", "statsPage"),
        Triple(FaIcons.User, "Profile", "profilePage")
    )

    // Get the current route from NavController's backstack
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF192126),
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
            )
            .padding(horizontal = 40.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, (icon, label, route) ->
            val isSelected = currentRoute == route

            Box(
                modifier = Modifier
                    .clickable {
                        if (currentRoute != route) {
                            navController.navigate(route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    // Active item with red circular background
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFED4747),
                                shape = CircleShape
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FaIcon(
                            icon,
                            tint = Color(0xFF192126),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = label,
                            color = Color(0xFF192126),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Inactive item (icon only)
                    FaIcon(
                        icon,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
