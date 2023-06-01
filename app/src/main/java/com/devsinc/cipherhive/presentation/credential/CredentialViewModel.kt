package com.devsinc.cipherhive.presentation.credential

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.cipherhive.domain.repository.CredentialRepository
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.use_case.crypto.SaveEncryptedCredential
import com.devsinc.cipherhive.domain.use_case.validate.ValidateLabel
import com.devsinc.cipherhive.domain.use_case.validate.ValidatePassword
import com.devsinc.cipherhive.domain.use_case.validate.ValidateUrl
import com.devsinc.cipherhive.domain.use_case.validate.ValidateUsername
import com.devsinc.cipherhive.domain.use_case.validate.ValidationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialViewModel @Inject constructor(
    private val saveCredential: SaveEncryptedCredential,
    private val validationUseCases: ValidationUseCases
) : ViewModel() {

    var state by mutableStateOf(CredentialFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: CredentialFormEvent) {
        when (event) {
            is CredentialFormEvent.OnLabelChanged -> {
                state = state.copy(label = event.label)
            }

            is CredentialFormEvent.OnUsernameChanged -> {
                state = state.copy(username = event.username)
            }

            is CredentialFormEvent.OnPasswordChanged -> {
                state = state.copy(password = event.password)
            }

            is CredentialFormEvent.OnUrlChanged -> {
                state = state.copy(url = event.url)
            }

            is CredentialFormEvent.OnNotesChanged -> {
                state = state.copy(notes = event.notes)
            }

            is CredentialFormEvent.OnSaveClicked -> {
                validateAndSaveCredential()
            }

            else -> {}
        }
    }

    private fun validateAndSaveCredential() {
        val labelResult = validationUseCases.validateLabel(state.label)
        val usernameResult = validationUseCases.validateUsername(state.username)
        val passwordResult = validationUseCases.validatePassword(state.password)
        val urlResult = validationUseCases.validateUrl(state.url)

        val hasError =
            listOf(labelResult, usernameResult, passwordResult, urlResult).any { !it.isValid }

        if (hasError) {
            state = state.copy(
                labelError = labelResult.errorMessage,
                usernameError = usernameResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                urlError = urlResult.errorMessage
            )
            return
        }

        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
            saveCredential(
                Credential(
                    label = state.label,
                    username = state.username,
                    password = state.password.toByteArray(),
                    url = state.url,
                    notes = state.notes
                )
            )
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
