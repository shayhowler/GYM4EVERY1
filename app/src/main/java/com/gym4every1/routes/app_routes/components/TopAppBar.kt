package com.gym4every1.routes.app_routes.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.guru.fontawesomecomposelib.FaIcon
import com.guru.fontawesomecomposelib.FaIcons
import com.gym4every1.R
import com.gym4every1.database.fetchProfilePicture
import com.gym4every1.database.fetchUserProfile
import com.gym4every1.models.social_models.FeedViewModel
import com.gym4every1.storage.uploadMedia
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    supabaseClient: SupabaseClient,
    showSavedVibes: Boolean, // Updated parameter name to reflect "Vibe"
    onShowSavedVibesChange: (Boolean) -> Unit, // Updated callback name
    showDivider: Boolean = true
) {
    val feedViewModel: FeedViewModel = viewModel()

    val userId = supabaseClient.auth.currentSessionOrNull()?.user?.id.toString()
    var username by remember { mutableStateOf("") }
    var profilePictureUrl by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isDialogOpen by remember { mutableStateOf(false) }
    var vibeText by remember { mutableStateOf("") } // Updated variable name
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
        }
    }

    LaunchedEffect(supabaseClient) {
        coroutineScope.launch {
            username = fetchUserProfile(supabaseClient, userId).toString()
            profilePictureUrl = fetchProfilePicture(supabaseClient)
        }
    }

    Column {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.White,
            ),
            title = {
                Image(
                    painter = painterResource(id = R.drawable.app_logo_round),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(50.dp)
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    onShowSavedVibesChange(!showSavedVibes) // Updated toggle logic
                    coroutineScope.launch {
                        feedViewModel.fetchVibes(supabaseClient, showSavedVibes)
                    }
                }) {
                    if (showSavedVibes) {
                        FaIcon(FaIcons.Bookmark)
                    } else {
                        FaIcon(FaIcons.BookmarkRegular)
                    }
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

    // Vibe Creation Dialog
    if (isDialogOpen) {
        AlertDialog(
            containerColor = Color.White,
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
                                    bucketName = "vibe_media", // Updated bucket name
                                    filename = filename,
                                    fileBytes = fileBytes
                                )
                            }
                        }

                        // Insert vibe into the database
                        supabaseClient.from("vibes").insert( // Updated table name
                            mapOf(
                                "user_id" to userId,
                                "username" to username,
                                "content" to vibeText,
                                "media_url" to mediaUrl
                            )
                        )

                        // Fetch updated vibes
                        feedViewModel.fetchVibes(supabaseClient)

                        // Reset UI and close dialog
                        vibeText = ""
                        selectedImageUri = null
                        isDialogOpen = false
                    }
                }) {
                    Text("Share Vibe", color = Color(0xFFED4747)) // Updated button text
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogOpen = false }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            title = {
                Text("Create Vibe", style = MaterialTheme.typography.titleLarge) // Updated title
            },
            text = {
                // Remaining UI logic is the same, with "vibe" replacing "post" where necessary
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(profilePictureUrl),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RectangleShape)
                                .border(1.dp, Color.Gray, RectangleShape),
                            contentScale = ContentScale.FillBounds
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(username, style = MaterialTheme.typography.bodyLarge)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    BasicTextField(
                        value = vibeText, // Updated variable
                        onValueChange = { vibeText = it }, // Updated logic
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(12.dp)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(15.dp)),
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { }),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                contentAlignment = Alignment.TopStart
                            ) {
                                if (vibeText.isEmpty()) {
                                    Text("What's your vibe?", color = Color.Gray) // Updated placeholder text
                                }
                                innerTextField()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { filePickerLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color(0xFFED4747),
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color(0xFFED4747))
                    ) {
                        Text("Add Photo")
                    }

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