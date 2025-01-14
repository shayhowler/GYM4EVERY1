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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gym4every1.database.fetchOwnerProfilePicture
import com.gym4every1.models.social_models.FeedViewModel
import com.gym4every1.routes.app_routes.components.PostCard
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    supabaseClient: SupabaseClient,
    paddingValues: PaddingValues,
    showSavedVibes: Boolean,
    viewModel: FeedViewModel = viewModel()
) {
    val context = LocalContext.current
    val activity = context as? Activity

    BackHandler {
        activity?.moveTaskToBack(true)
    }

    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    // Subscribe to real-time updates once the screen is loaded
    LaunchedEffect(Unit) {
        viewModel.subscribeToVibesAndLikes()
    }

    // Fetch vibes based on the showSavedVibes parameter
    LaunchedEffect(showSavedVibes) {
        viewModel.fetchVibes(supabaseClient, showSavedVibes)
    }

    val vibes = viewModel.vibes

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                viewModel.fetchVibes(supabaseClient, showSavedVibes)
                isRefreshing = false
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(vibes) { vibe ->
                var profilePictureUrl by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(vibe.user_id) {
                    val fetchedUrl = fetchOwnerProfilePicture(supabaseClient, vibe.user_id)
                    profilePictureUrl = fetchedUrl
                }

                val isSaved = viewModel.isVibeSaved(vibe.id)
                val isLiked = viewModel.isVibeLiked(vibe.id)

                PostCard(
                    profilePictureUrl = profilePictureUrl ?: "",
                    username = vibe.username,
                    content = vibe.content,
                    mediaUrl = vibe.media_url,
                    timestamp = vibe.created_at,
                    isSaved = isSaved,
                    isLiked,
                    likeCount = vibe.like_count ?: 0,
                    onMediaClick = { },
                    onLikeClick = {
                        coroutineScope.launch {
                            if (isLiked) {
                                viewModel.unlikeVibe(supabaseClient, vibe.id)
                            } else {
                                viewModel.likeVibe(supabaseClient, vibe.id)
                            }
                            viewModel.fetchVibes(supabaseClient, showSavedVibes) // Refresh vibes
                        }
                    },
                    onCommentClick = { },
                    onSaveClick = {
                        coroutineScope.launch {
                            if (isSaved) {
                                viewModel.unsaveVibe(supabaseClient, vibe.id)
                            } else {
                                viewModel.saveVibe(supabaseClient, vibe.id)
                            }
                            viewModel.fetchVibes(supabaseClient, showSavedVibes) // Refresh vibes
                        }
                    }
                )
            }
        }
    }
}