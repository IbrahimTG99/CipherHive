package com.devsinc.cipherhive.presentation.credential

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.use_case.crypto.DbUseCases
import com.devsinc.cipherhive.domain.use_case.validate.ValidationUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialViewModel @Inject constructor(
    private val db: DbUseCases, private val validationUseCases: ValidationUseCases
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

            is CredentialFormEvent.OnUpdateClicked -> {
                validateAndSaveCredential(event.credential)
            }

            else -> {}
        }
    }

    fun updateState(credential: Credential) {
        state = credential.notes?.let {
            state.copy(
                label = credential.label,
                username = credential.username,
                password = credential.password.toString(),
                url = credential.url,
                notes = it
            )
        }!!
    }

    fun resetState() {
        state = CredentialFormState()
    }

    private fun validateAndSaveCredential(
        credential: Credential? = null
    ) {
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

        if (credential == null) {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
                db.saveEncryptedCredential(
                    Credential(
                        label = state.label,
                        username = state.username,
                        password = state.password.toByteArray(),
                        url = state.url,
                        notes = state.notes
                    )
                )
            }
        } else {
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success)
                db.updateEncryptedCredential(
                    Credential(
                        id = credential.id,
                        label = state.label,
                        username = state.username,
                        password = state.password.toByteArray(),
                        url = state.url,
                        notes = state.notes
                    )
                )
            }
        }
    }

    private fun deleteCredential(
        credential: Credential
    ) {
        viewModelScope.launch {
            db.deleteCredential(credential)
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}
