package com.example.eshopapp.presentation

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.credentials.CredentialManager
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.example.eshopapp.R
import com.example.eshopapp.data.auth.AuthRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onAuthSuccess: () -> Unit
) {
    val context = LocalContext.current

    val credentialManager = remember {
        CredentialManager.create(context)
    }

    val authRepository = remember {
        AuthRepository()
    }

    val scope = rememberCoroutineScope()

    fun handleSignIn(credential: Credential) {
        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken

            authRepository.signInWithGoogleIdToken(
                idToken = idToken,
                onSuccess = {
                    Log.d("AuthScreen", "Firebase sign in success")
                    onAuthSuccess()
                },
                onError = { error ->
                    Log.e("AuthScreen", "Firebase sign in failed", error)
                }
            )

            Log.d("AuthScreen", "Google ID token received")
        } else {
            Log.d("AuthScreen", "Credential is not Google ID token")
        }
    }

    fun onGoogleSignInClick() {
        val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(
            serverClientId = context.getString(R.string.default_web_client_id)
        ).build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                e.printStackTrace()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { onGoogleSignInClick() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Войти через Google")
        }
    }
}