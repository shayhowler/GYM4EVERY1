package com.gym4every1.routes.app_routes.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.gym4every1.database.fetchFullName
import com.gym4every1.database.fetchProfilePicture
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import coil3.compose.rememberAsyncImagePainter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatsScreen(navController: NavController, supabaseClient: SupabaseClient, paddingValues: PaddingValues) {
    var fullName by remember { mutableStateOf("Loading...") }
    var profilePictureUrl by remember { mutableStateOf("") }
    val currentDate = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())

    LaunchedEffect(Unit) {
        val currentUserId = supabaseClient.auth.currentSessionOrNull()?.user?.id
        if (currentUserId != null) {
            fullName = fetchFullName(supabaseClient, currentUserId).toString()
            profilePictureUrl = fetchProfilePicture(supabaseClient).toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        // Header Row: Profile Picture and Welcome Message
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome 👋",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
                Text(
                    text = fullName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF546E7A)
                )
            }
            if (profilePictureUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(profilePictureUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // Today's Report Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Report",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF263238)
            )
            Text(
                text = currentDate,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nutrition Tracking Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { navController.navigate("detail/nutrition") },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.elevatedCardElevation(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Nutrition 🍎",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD84315)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Track your meals and calories for today",
                    fontSize = 18.sp,
                    color = Color(0xFFBF360C)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sleep and Water Tracking Cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Sleep Tracking Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .padding(8.dp)
                    .clickable { navController.navigate("detail/sleep") },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Sleep 😴",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "8 Hours of Sleep",
                        fontSize = 18.sp,
                        color = Color(0xFF0D47A1)
                    )
                }
            }

            // Water Tracking Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(140.dp)
                    .padding(8.dp)
                    .clickable { navController.navigate("detail/water") },
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.elevatedCardElevation(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Water 💧",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF388E3C)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "6/8 Cups",
                        fontSize = 18.sp,
                        color = Color(0xFF1B5E20)
                    )
                }
            }
        }
    }
}


