package com.devsinc.cipherhive

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devsinc.cipherhive.presentation.auth.Intro
import com.devsinc.cipherhive.presentation.home.Home
import com.devsinc.cipherhive.presentation.profile.ProfileScreen
import com.devsinc.cipherhive.presentation.settings.SettingsScreen
import com.devsinc.cipherhive.presentation.splash.Splash
import com.devsinc.cipherhive.ui.theme.CipherHiveTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            installSplashScreen()
        }

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
                    }
                    composable("home") {
                        Home(navController = navController)
                    }
                    composable("profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable("settings") {
                        SettingsScreen()
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
            val enrollIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
            } else {
                Intent(Settings.ACTION_SECURITY_SETTINGS)
            }
            activity.startActivityForResult(enrollIntent, 100)
        }
    }
}
