package com.devsinc.cipherhive.di

import android.content.Context
import com.devsinc.cipherhive.presentation.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideGoogleAuthUiClient(
        @ApplicationContext context: Context, auth: FirebaseAuth
    ): GoogleAuthUiClient {
        val oneTapClient = Identity.getSignInClient(context)
        return GoogleAuthUiClient(
            context = context, oneTapClient = oneTapClient, auth = auth
        )
    }

}
