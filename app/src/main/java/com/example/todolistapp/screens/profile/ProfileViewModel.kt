package com.example.todolistapp.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.AuthRepository
import com.example.todolistapp.data.ReloadUserResponse
import com.example.todolistapp.data.RevokeAccessResponse
import com.example.todolistapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var revokeAccessResponse by mutableStateOf<RevokeAccessResponse>(Response.Success(false))
        private set
    var reloadUserResponse by mutableStateOf<ReloadUserResponse>(Response.Success(false))
        private set

    fun reloadUser() = viewModelScope.launch {
        reloadUserResponse = Response.Loading
        reloadUserResponse = repo.reloadFirebaseUser()
    }

    val isEmailVerified get() = repo.currentUser?.isEmailVerified ?: false

    fun signOut() = repo.signOut()

    fun revokeAccess() = viewModelScope.launch {
        revokeAccessResponse = Response.Loading
        revokeAccessResponse = repo.revokeAccess()
    }
}