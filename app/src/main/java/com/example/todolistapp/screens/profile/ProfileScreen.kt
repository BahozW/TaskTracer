package com.example.todolistapp.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolistapp.R
import com.example.todolistapp.ui.components.ProgressBar
import com.example.todolistapp.ui.components.SmallSpacer
import com.example.todolistapp.ui.components.TopBar
import com.example.todolistapp.utils.Constants.ACCESS_REVOKED_MESSAGE
import com.example.todolistapp.utils.Constants.PROFILE_SCREEN
import com.example.todolistapp.utils.Constants.REVOKE_ACCESS_MESSAGE
import com.example.todolistapp.utils.Constants.SENSITIVE_OPERATION_MESSAGE
import com.example.todolistapp.utils.Constants.SIGN_OUT_ITEM
import com.example.todolistapp.utils.Constants.WELCOME_MESSAGE
import com.example.todolistapp.utils.Response
import com.example.todolistapp.utils.Utils.Companion.showMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToCalendar: () -> Unit,
    navigateToWeather: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopBar(
                title = PROFILE_SCREEN,
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        content = { padding ->
            ProfileContent(
                padding = padding,
                navigateToCalendar = navigateToCalendar,
                navigateToWeather = navigateToWeather
            )
        },
        scaffoldState = scaffoldState
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
fun ProfileContent(
    padding: PaddingValues,
    navigateToCalendar: () -> Unit,
    navigateToWeather: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(top = 48.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = WELCOME_MESSAGE,
                fontSize = 35.sp,
            )
            SmallSpacer()
            Image(
                painter = painterResource(R.drawable.picture),
                contentDescription = "App Icon",
                modifier = Modifier.size(200.dp)
            )
            SmallSpacer()
            Button(onClick = navigateToCalendar) {
                Text("Go To My Calendar")
            }
            SmallSpacer()
            Button(onClick = navigateToWeather) {
                Text("Weather info")
            }
        }
    }
}

@Composable
fun RevokeAccess(
    viewModel: ProfileViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    signOut: () -> Unit,
) {
    val context = LocalContext.current

    fun showRevokeAccessMessage() = coroutineScope.launch {
        val result = scaffoldState.snackbarHostState.showSnackbar(
            message = REVOKE_ACCESS_MESSAGE,
            actionLabel = SIGN_OUT_ITEM
        )
        if (result == SnackbarResult.ActionPerformed) {
            signOut()
        }
    }

    when(val revokeAccessResponse = viewModel.revokeAccessResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> {
            val isAccessRevoked = revokeAccessResponse.data
            LaunchedEffect(isAccessRevoked) {
                if (isAccessRevoked) {
                    showMessage(context, ACCESS_REVOKED_MESSAGE)
                }
            }
        }
        is Response.Failure -> revokeAccessResponse.apply {
            LaunchedEffect(e) {
                print(e)
                if (e.message == SENSITIVE_OPERATION_MESSAGE) {
                    showRevokeAccessMessage()
                }
            }
        }
    }
}