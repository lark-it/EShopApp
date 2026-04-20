package com.example.eshopapp.data.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

data class UserProfile(
    val name: String?,
    val email: String?,
    val photoUrl: String?
)

class AuthRepository {

    private val auth: FirebaseAuth = Firebase.auth

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun signOut() {
        auth.signOut()
    }

    fun signInWithGoogleIdToken(
        idToken: String,
        onSuccess: () -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception)
                }
            }
    }

    fun getCurrentUserProfile(): UserProfile? {
        val user = auth.currentUser ?: return null

        return UserProfile(
            name = user.displayName,
            email = user.email,
            photoUrl = user.photoUrl?.toString()
        )
    }
}