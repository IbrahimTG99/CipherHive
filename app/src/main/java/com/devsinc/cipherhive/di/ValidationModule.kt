package com.devsinc.cipherhive.di

import com.devsinc.cipherhive.domain.use_case.validate.ValidateLabel
import com.devsinc.cipherhive.domain.use_case.validate.ValidatePassword
import com.devsinc.cipherhive.domain.use_case.validate.ValidateUrl
import com.devsinc.cipherhive.domain.use_case.validate.ValidateUsername
import com.devsinc.cipherhive.domain.use_case.validate.ValidationUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ValidationModule {

    @Provides
    fun provideValidateLabel() = ValidateLabel()

    @Provides
    fun provideValidateUsername() = ValidateUsername()

    @Provides
    fun provideValidatePassword() = ValidatePassword()

    @Provides
    fun provideValidateUrl() = ValidateUrl()

    @Provides
    fun provideValidationUseCases(
        validateLabel: ValidateLabel,
        validateUsername: ValidateUsername,
        validatePassword: ValidatePassword,
        validateUrl: ValidateUrl
    ) = ValidationUseCases(
        validateLabel = validateLabel,
        validateUsername = validateUsername,
        validatePassword = validatePassword,
        validateUrl = validateUrl
    )
}
