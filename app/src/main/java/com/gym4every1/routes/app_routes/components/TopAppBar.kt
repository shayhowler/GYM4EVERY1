package com.gym4every1.routes.app_routes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(showDivider: Boolean = true) {
    Column {
        // Top App Bar
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFFFAFAFA),
                titleContentColor = Color(0xFFFAFAFA),
            ),
            title = {
                Image(
                    painter = painterResource(id = R.drawable.app_logo_round),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(50.dp) // Adjust size as needed
                )
            },
            navigationIcon = {
                IconButton(onClick = { /* do something */ }) {
                    FaIcon(FaIcons.List)
                }
            },
            actions = {
                IconButton(onClick = { /* do something */ }) {
                    FaIcon(FaIcons.PaperPlaneRegular)
                }
            },
        )

        // Add HorizontalDivider if the flag is set to true
        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }
}