package com.devsinc.cipherhive

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devsinc.cipherhive.data.repository.CredentialRepository
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.presentation.auth.Intro
import com.devsinc.cipherhive.presentation.auth.Login
import com.devsinc.cipherhive.presentation.auth.Register
import com.devsinc.cipherhive.presentation.home.Home
import com.devsinc.cipherhive.presentation.sign_in.GoogleAuthUiClient
import com.devsinc.cipherhive.presentation.sign_in.SignInViewModel
import com.devsinc.cipherhive.presentation.splash.Splash
import com.devsinc.cipherhive.ui.theme.CipherHiveTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()

        setContent {
            CipherHiveTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "splash",
                ) {
                    composable("splash") {
                        Splash(navController)
                    }
                    composable("intro") {
                        Intro(navController)
                        LaunchedEffect(key1 = Unit) {
                            if (googleAuthUiClient.getSignedInUser() != null) {
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                            }
                        }
                    }
                    composable("login") {
                        val viewModel: SignInViewModel by viewModels()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        val launcher =
                            rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                })

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if (state.isSignInSuccessful) {
                                Toast.makeText(
                                    this@MainActivity, "Sign in successful", Toast.LENGTH_LONG
                                ).show()
                                navController.navigate("home") {
                                    popUpTo(0)
                                }
                                viewModel.resetState()
                            }
                        }

                        Login(state = state, onSignInClick = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }, navController = navController)
                    }
                    composable("register") {
                        Register(navController)
                    }
                    composable("home" ){
                        Home()
                    }
                }
            }
        }

//        setContent {
//            CipherHiveTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
//                ) {
//                    val navController = rememberNavController()
//                    NavHost(navController = navController, startDestination = "sign_in") {
//                        composable("sign_in") {
//                            val viewModel: SignInViewModel by viewModels()
//                            val state by viewModel.state.collectAsStateWithLifecycle()
//
//                            LaunchedEffect(key1 = Unit) {
//                                if (googleAuthUiClient.getSignedInUser() != null) {
//                                    navController.navigate("profile")
//                                }
//                            }
//
//                            val launcher =
//                                rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
//                                    onResult = { result ->
//                                        if (result.resultCode == RESULT_OK) {
//                                            lifecycleScope.launch {
//                                                val signInResult =
//                                                    googleAuthUiClient.signInWithIntent(
//                                                        intent = result.data ?: return@launch
//                                                    )
//                                                viewModel.onSignInResult(signInResult)
//                                            }
//                                        }
//                                    })
//
//                            LaunchedEffect(key1 = state.isSignInSuccessful) {
//                                if (state.isSignInSuccessful) {
//                                    Toast.makeText(
//                                        this@MainActivity, "Sign in successful", Toast.LENGTH_LONG
//                                    ).show()
//                                    navController.navigate("profile")
//                                    viewModel.resetState()
//                                }
//                            }
//
//                            SignInScreen(state = state, onSignInClick = {
//                                lifecycleScope.launch {
//                                    val signInIntentSender = googleAuthUiClient.signIn()
//                                    launcher.launch(
//                                        IntentSenderRequest.Builder(
//                                            signInIntentSender ?: return@launch
//                                        ).build()
//                                    )
//                                }
//                            })
//                        }
//
//                        composable("profile") {
//                            ProfileScreen(
//                                userData = googleAuthUiClient.getSignedInUser(),
//                                onSignOut = {
//                                    lifecycleScope.launch {
//                                        googleAuthUiClient.signOut()
//                                        Toast.makeText(
//                                            this@MainActivity,
//                                            "Sign out successful",
//                                            Toast.LENGTH_LONG
//                                        ).show()
//
//                                        navController.popBackStack()
//                                    }
//                                })
//                        }
//                    }
//                }
//            }
//        }
    }
}
