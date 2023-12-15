package com.example.todolistapp.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.AuthRepository
import com.example.todolistapp.data.LoginResponse
import com.example.todolistapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    var loginResponse by mutableStateOf<LoginResponse>(Response.Success(false))
        private set

    fun loginWithEmailAndPassword(email: String, password: String) = viewModelScope.launch {
        loginResponse = Response.Loading
        loginResponse = repo.firebaseLoginWithEmailAndPassword(email, password)
    }
}