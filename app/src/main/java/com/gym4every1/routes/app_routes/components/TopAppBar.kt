package com.gym4every1.routes.app_routes.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.database.fetchProfilePicture
import com.gym4every1.database.fetchUserProfile
import com.gym4every1.storage.uploadMedia
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    supabaseClient: SupabaseClient,
    showDivider: Boolean = true
) {
    val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id.toString()
    var username by remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isDialogOpen by remember { mutableStateOf(false) }
    var postText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }

    // Fetch username and profile picture when TopBar is composed
    LaunchedEffect(supabaseClient) {
        coroutineScope.launch {
            username = fetchUserProfile(supabaseClient, userId).toString()
            profilePictureUrl = fetchProfilePicture(supabaseClient)
        }
    }

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
                IconButton(onClick = { /* Add functionality if needed */ }) {
                    FaIcon(FaIcons.List)
                }
            },
            actions = {
                IconButton(onClick = { isDialogOpen = true }) {
                    FaIcon(FaIcons.PaperPlaneRegular)
                }
            },
        )

        if (showDivider) {
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
            )
        }
    }

    // Post Creation Dialog
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        var mediaUrl: String? = null

                        // Upload image if selected
                        selectedImageUri?.let { uri ->
                            val contentResolver = context.contentResolver
                            val inputStream = contentResolver.openInputStream(uri)
                            inputStream?.use { stream ->
                                val fileBytes = stream.readBytes()
                                val filename = uri.lastPathSegment ?: "uploaded_file.png"

                                // Upload to Supabase
                               mediaUrl = uploadMedia(
                                    supabaseClient = supabaseClient,
                                    bucketName = "post_media",
                                    filename = filename,
                                    fileBytes = fileBytes
                                )
                            }
                        }

                        // Insert post into the database
                        supabaseClient.from("posts").insert(
                            mapOf(
                                "user_id" to userId,
                                "username" to username,
                                "content" to postText,
                                "media_url" to mediaUrl
                            )
                        )

                        // Reset UI and close dialog
                        postText = ""
                        selectedImageUri = null
                        isDialogOpen = false
                    }
                }) {
                    Text("Post", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogOpen = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            title = {
                Text("Create Post", style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // User Profile Image and Username
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(profilePictureUrl),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(username, style = MaterialTheme.typography.bodyLarge)
                    }

                    // Text Input
                    Spacer(modifier = Modifier.height(16.dp))
                    BasicTextField(
                        value = postText,
                        onValueChange = { postText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(12.dp)
                            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { /* Handle enter press */ }),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentAlignment = Alignment.TopStart
                            ) {
                                if (postText.isEmpty()) {
                                    Text("What's happening?", color = Color.Gray)
                                }
                                innerTextField()
                            }
                        }
                    )

                    // Add Image Button
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { filePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Photo")
                    }

                    // Selected Image Preview
                    selectedImageUri?.let { uri ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        )
    }
}