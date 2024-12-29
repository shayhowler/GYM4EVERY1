package com.gym4every1.routes.auth_routes.start_routes

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.gym4every1.routes.auth_routes.shared.RectBgButton
import com.gym4every1.singletons.ProfileViewModel
import com.gym4every1.storage.uploadMedia
import io.github.jan.supabase.SupabaseClient
import kotlinx.coroutines.launch

@Composable
fun ProfilePictureAndGenderSelectionPage(
    navController: NavController,
    supabaseClient: SupabaseClient
) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler {
        Toast.makeText(context, "You can't go back!", Toast.LENGTH_SHORT).show()
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ProfileViewModel.profilePictureUrl = it.toString() // Update ViewModel with image URI as string
        }
    }

    // Gender options and selected gender state
    val genderOptions = listOf("Male", "Female")
    var selectedGender by remember { mutableStateOf(ProfileViewModel.gender ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Profile Image Selection
        Image(
            painter = rememberAsyncImagePainter(selectedImageUri ?: ProfileViewModel.profilePictureUrl),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
                .clickable { filePickerLauncher.launch("image/*") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Gender Selection with Radio Buttons
        Text(text = "Select Gender", modifier = Modifier.align(Alignment.Start))
        genderOptions.forEach { gender ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedGender = gender
                        ProfileViewModel.gender = gender // Update ViewModel with selected gender
                    }
                    .padding(vertical = 8.dp)
            ) {
                RadioButton(
                    selected = selectedGender == gender,
                    onClick = {
                        selectedGender = gender
                        ProfileViewModel.gender = gender // Update ViewModel with selected gender
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = gender)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button
        RectBgButton(
            onClick = {
                coroutineScope.launch {
                    selectedImageUri?.let { uri ->
                        val contentResolver = context.contentResolver
                        val inputStream = contentResolver.openInputStream(uri)
                        inputStream?.use { stream ->
                            val fileBytes = stream.readBytes() // Read the file's byte data
                            val filename = uri.lastPathSegment ?: "uploaded_file.png"
                            val bucketName = "profile_pictures" // Set your bucket name

                            try {
                                // Call the upload function from the storage module
                                val publicUrl = uploadMedia(supabaseClient, bucketName, filename, fileBytes)
                                ProfileViewModel.profilePictureUrl = publicUrl
                                Toast.makeText(context, "Profile picture uploaded successfully", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error uploading picture: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        } ?: run {
                            Toast.makeText(context, "Invalid file selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            buttonText = "Save",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Continue Button
        RectBgButton(
            onClick = {
                navController.navigate("weightPage")
            },
            buttonText = "Continue",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
        )
    }
}