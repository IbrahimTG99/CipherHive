package com.devsinc.cipherhive.presentation.credential

import com.devsinc.cipherhive.domain.model.Credential

sealed class CredentialFormEvent {
    data class OnLabelChanged(val label: String) : CredentialFormEvent()
    data class OnUsernameChanged(val username: String) : CredentialFormEvent()
    data class OnPasswordChanged(val password: String) : CredentialFormEvent()
    data class OnUrlChanged(val url: String) : CredentialFormEvent()
    data class OnNotesChanged(val notes: String) : CredentialFormEvent()
    object OnSaveClicked : CredentialFormEvent()
    data class OnUpdateClicked(val credential: Credential) : CredentialFormEvent()
}
