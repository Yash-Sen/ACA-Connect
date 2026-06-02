package com.example.acaconnect.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acaconnect.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    
    private val _userState = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val userState: StateFlow<FirebaseUser?> = _userState

    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile

    private val _profileError = MutableStateFlow<String?>(null)
    val profileError: StateFlow<String?> = _profileError

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            _userState.value = user
            if (user != null) {
                fetchUserProfile(user.uid)
            } else {
                _userProfile.value = null
            }
        }
    }

    private fun fetchUserProfile(uid: String) {
        _profileError.value = null
        viewModelScope.launch {
            try {
                val document = firestore.collection("users").document(uid).get().await()
                if (document.exists()) {
                    _userProfile.value = document.toObject(UserProfile::class.java)
                } else {
                    _profileError.value = "Profile document missing in Firestore. Please try signing up again."
                }
            } catch (e: Exception) {
                _profileError.value = "Database Error: ${e.message}"
            }
        }
    }

    fun signUp(profile: UserProfile, pass: String, imageUri: Uri?, onResult: (String?) -> Unit) {
        auth.createUserWithEmailAndPassword(profile.email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: ""
                    
                    if (imageUri != null) {
                        uploadProfileImage(uid, imageUri) { imageUrl ->
                            val finalProfile = profile.copy(uid = uid, profileImageUrl = imageUrl ?: "")
                            saveUserToFirestore(finalProfile, onResult)
                        }
                    } else {
                        val finalProfile = profile.copy(uid = uid)
                        saveUserToFirestore(finalProfile, onResult)
                    }
                } else {
                    onResult(task.exception?.message ?: "Sign up failed")
                }
            }
    }

    private fun uploadProfileImage(uid: String, uri: Uri, onComplete: (String?) -> Unit) {
        val ref = storage.reference.child("profile_images/$uid.jpg")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { downloadUri ->
                    onComplete(downloadUri.toString())
                }.addOnFailureListener {
                    onComplete(null)
                }
            }
            .addOnFailureListener {
                onComplete(null)
            }
    }

    private fun saveUserToFirestore(profile: UserProfile, onResult: (String?) -> Unit) {
        firestore.collection("users").document(profile.uid).set(profile)
            .addOnCompleteListener { firestoreTask ->
                if (firestoreTask.isSuccessful) {
                    _userProfile.value = profile
                    onResult(null)
                } else {
                    onResult(firestoreTask.exception?.message ?: "Failed to save profile")
                }
            }
    }

    fun updateProfile(profile: UserProfile, onResult: (String?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).set(profile)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userProfile.value = profile
                    onResult(null)
                } else {
                    onResult(task.exception?.message ?: "Update failed")
                }
            }
    }

    fun signIn(email: String, pass: String, onResult: (String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(null)
                } else {
                    onResult(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
}
