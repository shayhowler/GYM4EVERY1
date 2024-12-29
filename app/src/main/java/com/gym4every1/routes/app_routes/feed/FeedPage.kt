package com.gym4every1.routes.app_routes.feed

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gym4every1.database.fetchOwnerProfilePicture
import com.gym4every1.models.social_models.FeedViewModel
import com.gym4every1.routes.app_routes.components.PostCard
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(supabaseClient: SupabaseClient, paddingValues: PaddingValues, viewModel: FeedViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity

    BackHandler {
        activity?.moveTaskToBack(true) // Moves the app to the background
    }

    // State for Pull Refresh and Coroutine Scope for refreshing data.
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch posts when the screen is first displayed.
    LaunchedEffect(Unit) {
        viewModel.fetchPosts(supabaseClient)
    }

    val posts = viewModel.posts

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                viewModel.fetchPosts(supabaseClient)
                isRefreshing = false // Reset refreshing state after fetching data.
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(paddingValues) // Ensure padding around the screen content
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize() // Make sure LazyColumn fills the screen
                .padding(horizontal = 5.dp) // No horizontal padding here
        ) {
            items(posts) { post ->
                // Fetch the profile picture URL for each post's user.
                var profilePictureUrl by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(post.user_id) { // Use user_id from post for fetching.
                    val fetchedUrl = fetchOwnerProfilePicture(supabaseClient, post.user_id)
                    profilePictureUrl = fetchedUrl // Store fetched URL.
                }

                PostCard(
                    profilePictureUrl = profilePictureUrl ?: "", // Use the fetched profile picture URL.
                    username = post.username,
                    content = post.content,
                    mediaUrl = post.media_url,
                    timestamp = post.created_at,
                    onMediaClick = {
                        // Handle media click.
                    },
                    onLikeClick = {
                        // Handle like click.
                    },
                    onCommentClick = {
                        // Handle comment click.
                    }
                )
            }
        }
    }
}