package com.gym4every1.routes.feed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.gym4every1.auth.logoutUser
import com.gym4every1.routes.shared.RectBgButton
import com.gym4every1.routes.shared.Routes
import com.gym4every1.singletons.SupabaseClientManager
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedPageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val supabaseClient = SupabaseClientManager.getSupabaseClient(this)

        setContent {
            FeedPageScreen(
                supabaseClient = supabaseClient,
                onNavigate = { navigateTo ->
                    when (navigateTo) {
                        "pageAuthHome" -> Routes.navigateToAuthHome(this)
                    }
                },
            )
        }
    }
}

@Composable
fun FeedPageScreen(
    supabaseClient: SupabaseClient,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Feed Content
        FeedContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Reserve space for the logout button
        )

        // Logout Button
        RectBgButton(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    logoutUser(supabaseClient)
                }
                onNavigate("pageAuthHome")
            },
            buttonText = "Log Out",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun FeedContent(modifier: Modifier = Modifier) {
    val feedItems = listOf(
        FeedItem(
            userName = "John Doe",
            description = "Just completed a 5k run today! Feeling great 💪",
            imageUrl = "https://images.pexels.com/photos/936094/pexels-photo-936094.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        FeedItem(
            userName = "Jamey Smith",
            description = "Loving this yoga session at the park 🌿🧘‍♀️",
            imageUrl = "https://images.pexels.com/photos/6787357/pexels-photo-6787357.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        ),
        FeedItem(
            userName = "David Brown",
            description = "Hit a new personal best on the bench press today! 🏋️‍♂️",
            imageUrl = "https://images.pexels.com/photos/3490363/pexels-photo-3490363.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        items(feedItems.size) { index ->
            FeedCard(feedItem = feedItems[index])
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun FeedCard(feedItem: FeedItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // User name
            Text(
                text = feedItem.userName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )

            // Description
            Text(
                text = feedItem.description,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Image
            Image(
                painter = rememberAsyncImagePainter(model = feedItem.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 8.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}
data class FeedItem(
    val userName: String,
    val description: String,
    val imageUrl: String
)