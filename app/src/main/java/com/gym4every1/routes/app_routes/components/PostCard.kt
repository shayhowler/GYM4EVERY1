package com.gym4every1.routes.app_routes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    onMediaClick: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Ensure the post card takes full width
            .padding(8.dp)
    ) {
        // Header with Profile Picture, Username, and Timestamp
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth() // Ensure header takes full width
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            // Profile Picture
            Image(
                painter = rememberAsyncImagePainter(profilePictureUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .size(40.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                // Username
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Timestamp
                Text(
                    text = formatTimestamp(timestamp),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        // Content/Text Section
        if (content.isNotEmpty()) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth() // Ensure content takes full width
                    .padding(horizontal = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Space between content and media

        // Media Section
        mediaUrl?.let { url ->
            Image(
                painter = rememberAsyncImagePainter(url),
                contentDescription = "Post Media",
                modifier = Modifier
                    .fillMaxWidth() // Ensure media image takes full width
                    .height(300.dp)
                    .clickable { onMediaClick() },
                contentScale = ContentScale.FillBounds
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Space between media and interactions

        // Interaction Row: Like, Comment
        Row(
            modifier = Modifier
                .fillMaxWidth() // Ensure interaction row takes full width
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Like Button
                IconButton(onClick = onLikeClick) {
                    FaIcon(FaIcons.HeartRegular)
                }

                // Comment Button
                IconButton(onClick = onCommentClick) {
                    FaIcon(FaIcons.CommentRegular)
                }
            }

            // Placeholder for Save/Share (Optional)
            FaIcon(
                FaIcons.BookmarkRegular,
                modifier = Modifier.clickable { /* Save action */ }
            )
        }
    }
}