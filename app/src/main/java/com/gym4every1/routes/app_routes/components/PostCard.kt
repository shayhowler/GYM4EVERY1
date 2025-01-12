package com.gym4every1.routes.app_routes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.database.formatTimestamp

@Composable
fun PostCard(
    profilePictureUrl: String,
    username: String,
    content: String,
    mediaUrl: String?,
    timestamp: String,
    isSaved: Boolean,
    isLiked: Boolean,
    likeCount: Int, // Add the like count parameter
    onMediaClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(10.dp),
        shape = RoundedCornerShape(0.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.onSurface,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(profilePictureUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .clip(RectangleShape)
                        .size(60.dp),
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = username,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = formatTimestamp(timestamp),
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // Content
            if (content.isNotEmpty()) {
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Media
            mediaUrl?.let { url ->
                Image(
                    painter = rememberAsyncImagePainter(url),
                    contentDescription = "Post Media",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clickable { onMediaClick() },
                    contentScale = ContentScale.FillBounds
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Interaction Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = onLikeClick) {
                        FaIcon(
                            if(isLiked) FaIcons.Heart else FaIcons.HeartRegular)
                    }

                    // Like count text
                    Text(
                        text = "$likeCount Likes",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )

                    IconButton(onClick = onCommentClick) {
                        FaIcon(FaIcons.CommentRegular)
                    }
                }

                // Save/Unsave Button
                IconButton(onClick = onSaveClick) {
                    FaIcon(
                        if (isSaved) FaIcons.Bookmark else FaIcons.BookmarkRegular
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}