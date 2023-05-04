package com.devsinc.cipherhive

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devsinc.cipherhive.model.UserDatum
import com.devsinc.cipherhive.model.UserDatumSerializer
import com.devsinc.cipherhive.presentation.profile.ProfileScreen
import com.devsinc.cipherhive.presentation.sign_in.GoogleAuthUiClient
import com.devsinc.cipherhive.presentation.sign_in.SignInScreen
import com.devsinc.cipherhive.presentation.sign_in.SignInViewModel
import com.devsinc.cipherhive.ui.theme.CipherHiveTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    private val Context.dataStore: DataStore<UserDatum> by dataStore(
        fileName = "user-datum.json", serializer = UserDatumSerializer(CryptoManager())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cryptoManager = CryptoManager()

        setContent {
            CipherHiveTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in") {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() != null) {
                                    navController.navigate("profile")
                                }
                            }

                            val launcher =
                                rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = { result ->
                                        if (result.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val signInResult =
                                                    googleAuthUiClient.signInWithIntent(
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
                                    navController.navigate("profile")
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(state = state, onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            })
                        }

                        composable("profile") {
                            ProfileScreen(
                                userData = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Sign out successful",
                                            Toast.LENGTH_LONG
                                        ).show()

                                        navController.popBackStack()
                                    }
                                },
                                dataStore = dataStore
                            )
                        }
                    }
                }
            }
        }
    }
}
