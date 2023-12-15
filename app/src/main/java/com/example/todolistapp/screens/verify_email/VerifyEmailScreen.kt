package com.example.todolistapp.screens.verify_email

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolistapp.ui.components.ProgressBar
import com.example.todolistapp.ui.components.SmallSpacer
import com.example.todolistapp.ui.components.TopBar
import com.example.todolistapp.screens.profile.ProfileViewModel
import com.example.todolistapp.screens.profile.RevokeAccess
import com.example.todolistapp.utils.Constants.ALREADY_VERIFIED
import com.example.todolistapp.utils.Constants.EMAIL_NOT_VERIFIED_MESSAGE
import com.example.todolistapp.utils.Constants.SPAM_EMAIL
import com.example.todolistapp.utils.Constants.VERIFY_EMAIL_SCREEN
import com.example.todolistapp.utils.Response
import com.example.todolistapp.utils.Utils.Companion.showMessage

@Composable
fun VerifyEmailScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopBar(
                title = VERIFY_EMAIL_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        content = { padding ->
            VerifyEmailContent(
                padding = padding,
                reloadUser = {
                    viewModel.reloadUser()
                }
            )
        },
        scaffoldState = scaffoldState
    )

    ReloadUser(
        navigateToProfileScreen = {
            if (viewModel.isEmailVerified) {
                navigateToProfileScreen()
            } else {
                showMessage(context, EMAIL_NOT_VERIFIED_MESSAGE)
            }
        }
    )

    RevokeAccess(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope,
        signOut = {
            viewModel.signOut()
        }
    )
}

@Composable
fun VerifyEmailContent(
    padding: PaddingValues,
    reloadUser: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(padding).padding(start = 32.dp, end = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.clickable {
                reloadUser()
            },
            text = ALREADY_VERIFIED,
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
        SmallSpacer()
        Text(
            text = SPAM_EMAIL,
            fontSize = 15.sp
        )
    }
}

@Composable
fun ReloadUser(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToProfileScreen: () -> Unit
) {
    when(val reloadUserResponse = viewModel.reloadUserResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            val isUserReloaded = reloadUserResponse.data
            LaunchedEffect(isUserReloaded) {
                if (isUserReloaded) {
                    navigateToProfileScreen()
                }
            }
        }
        is Response.Failure -> reloadUserResponse.apply {
            LaunchedEffect(e) {
                print(e)
            }
        }
    }
}