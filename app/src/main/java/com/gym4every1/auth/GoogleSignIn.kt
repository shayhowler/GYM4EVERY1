package com.gym4every1.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.gym4every1.BuildConfig
import com.gym4every1.R
import com.gym4every1.models.auth_models.Profile
import com.gym4every1.models.auth_models.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun GoogleSignInButton(
    navController: NavController,
    supabase: SupabaseClient,
    context: Context,
    onSignInStarted: () -> Unit = {},
    onSignInCompleted: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        // Generate a nonce and hash it
        val rawNonce = UUID.randomUUID().toString()
        val hashedNonce = MessageDigest.getInstance("SHA-256")
            .digest(rawNonce.toByteArray())
            .joinToString("") { "%02x".format(it) }

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setNonce(hashedNonce)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            onSignInStarted() // Notify that sign-in has started
            try {
                val result = credentialManager.getCredential(request = request, context = context)
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                val googleIdToken = googleIdTokenCredential.idToken

                supabase.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                val identities = supabase.auth.currentIdentitiesOrNull()
                if (identities != null && identities.isNotEmpty()) {
                    val identityData = identities[0].identityData
                    val userId = supabase.auth.currentSessionOrNull()?.user?.id ?: return@launch
                    val email = identityData["email"]?.toString()?.replace("\"", "") ?: ""

                    // Check if the user exists
                    val existingUsers = supabase.from("users")
                        .select(columns = Columns.list("id, email, username"))
                        .decodeList<User>()

                    val existingUser = existingUsers.firstOrNull { it.email == email }

                    if (existingUser != null) {
                        if (existingUser.username == null) {
                            navController.navigate("signUp1")
                        } else {
                            val existingProfiles = supabase.from("profiles")
                                .select(columns = Columns.list("id, username, profile_picture_url, gender, weight, height, dateofbirth, activity_level, weight_goal"))
                                .decodeList<Profile>()

                            val userProfile = existingProfiles.firstOrNull { it.username == existingUser.username }

                            if (userProfile?.weight != null) {
                                navController.navigate("transitionPage1")
                                Toast.makeText(context, "Sign-In Successful!", Toast.LENGTH_SHORT).show()
                            } else {
                                navController.navigate("getStarted")
                                Toast.makeText(context, "You must configure your profile first!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        supabase.from("users").insert(
                            mapOf(
                                "id" to userId,
                                "email" to email,
                                "fullname" to null,
                                "username" to null,
                                "securityquestion" to null,
                                "answer" to null
                            )
                        )
                        navController.navigate("signUp1")
                        Toast.makeText(context, "Success! Now you should setup your account", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No identities found.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.d("Error:", "${e.localizedMessage}")
            } finally {
                onSignInCompleted() // Notify that sign-in is completed
            }
        }
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(280.dp)
                .height(56.dp)
                .background(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(30.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).padding(end = 8.dp)
                )
                Text(
                    text = "Sign in with Google",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.lato))
                )
            }
        }
    }
}