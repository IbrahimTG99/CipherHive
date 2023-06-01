package com.devsinc.cipherhive.presentation.credential

sealed class CredentialFormEvent {
    data class OnLabelChanged(val label: String) : CredentialFormEvent()
    data class OnUsernameChanged(val username: String) : CredentialFormEvent()
    data class OnPasswordChanged(val password: String) : CredentialFormEvent()
    data class OnUrlChanged(val url: String) : CredentialFormEvent()
    data class OnNotesChanged(val notes: String) : CredentialFormEvent()
    object OnSaveClicked : CredentialFormEvent()
}
