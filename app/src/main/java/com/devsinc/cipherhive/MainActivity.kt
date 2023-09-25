package com.devsinc.cipherhive

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devsinc.cipherhive.presentation.auth.Intro
import com.devsinc.cipherhive.presentation.auth.Login
import com.devsinc.cipherhive.presentation.auth.Register
import com.devsinc.cipherhive.presentation.home.Home
import com.devsinc.cipherhive.presentation.profile.ProfileScreen
import com.devsinc.cipherhive.presentation.sign_in.GoogleAuthUiClient
import com.devsinc.cipherhive.presentation.sign_in.SignInViewModel
import com.devsinc.cipherhive.presentation.splash.Splash
import com.devsinc.cipherhive.ui.theme.CipherHiveTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    @Inject
    lateinit var googleAuthUiClient: GoogleAuthUiClient

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            installSplashScreen()
        }

        setContent {
            CipherHiveTheme {
//                Authenticate()
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
                    composable("home") {
                        Home(navController = navController)
                    }
                    composable("profile") {
                        ProfileScreen(userData = googleAuthUiClient.getSignedInUser(),
                            onSignOut = {
                                lifecycleScope.launch {
                                    googleAuthUiClient.signOut()
                                    Toast.makeText(
                                        this@MainActivity, "Sign out successful", Toast.LENGTH_LONG
                                    ).show()

                                    navController.popBackStack()
                                }
                            })
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun Authenticate() {
    val context = LocalContext.current
    val activity = LocalContext.current as FragmentActivity
    val executor = ContextCompat.getMainExecutor(activity)

    CheckBiometric(context = context, activity = activity)

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric login for my app")
        .setSubtitle("Log in using your biometric credential")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    val biometricPrompt =
        BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    context,
                    "Authentication succeeded!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(
                    context, "Authentication failed", Toast.LENGTH_SHORT
                ).show()
            }
        })

    LaunchedEffect(Unit) {
        delay(500)
        biometricPrompt.authenticate(promptInfo)
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun CheckBiometric(context: Context, activity: FragmentActivity) {
    val biometricManager = BiometricManager.from(context)
    when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
        BiometricManager.BIOMETRIC_SUCCESS ->
            Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
            Log.e("MY_APP_TAG", "No biometric features available on this device.")

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
            Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            // Prompts the user to create credentials that your app accepts.
            val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                )
            }

            startActivityForResult(activity, enrollIntent, 1234, null)
        }
    }
}
