package com.gym4every1.auth

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.gym4every1.BuildConfig
import com.gym4every1.R
import com.gym4every1.routes.start_routes.GetStartedActivity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.IDToken
import io.github.jan.supabase.exceptions.RestException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun GoogleSignInButton(supabase: SupabaseClient) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val onClick: () -> Unit = {
        val credentialManager = CredentialManager.create(context)

        // Generate a nonce and hash it with sha-256
        val rawNonce = UUID.randomUUID().toString() // Generate a random String
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) } // Hashed nonce

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID) // Use BuildConfig for Google Client ID
            .setNonce(hashedNonce) // Provide the nonce
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(result.credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                supabase.auth.signInWith(IDToken) {
                    idToken = googleIdToken
                    provider = Google
                    nonce = rawNonce
                }

                // Handle successful sign-in
                Toast.makeText(context, "Sign-In Successful!", Toast.LENGTH_SHORT).show()

                // Start new Activity instead of using navController
                val intent = Intent(context, GetStartedActivity::class.java)
                context.startActivity(intent)

            } catch (e: GetCredentialException) {
                // Handle GetCredentialException and show error toast
                Toast.makeText(context, "Credential error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } catch (e: GoogleIdTokenParsingException) {
                // Handle GoogleIdTokenParsingException and show error toast
                Toast.makeText(context, "Google Token error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } catch (e: RestException) {
                // Handle RestException and show error toast
                Toast.makeText(context, "Supabase error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Handle unknown exceptions and show error toast
                Toast.makeText(context, "Unknown error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent // Make the background transparent
        )
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(280.dp)
                .height(56.dp)
                .background(
                    color = Color.Transparent, // Ensure the background is transparent
                    shape = RoundedCornerShape(30.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.Black, // Border color
                    shape = RoundedCornerShape(30.dp)
                )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.google_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = stringResource(R.string.sign_in_with_google),
                    color = Color.Black, // Text color
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.lato))
                )
            }
        }
    }
}