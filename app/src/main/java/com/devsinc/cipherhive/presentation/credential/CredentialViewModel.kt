package com.devsinc.cipherhive.presentation.credential

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.cipherhive.data.repository.CredentialRepository
import com.devsinc.cipherhive.domain.model.Credential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CredentialViewModel @Inject constructor(
    private val repository: CredentialRepository
) : ViewModel() {

    fun insertCredential(credential: Credential) = viewModelScope.launch {
        repository.insertCredential(credential)
    }

    fun deleteCredential(credential: Credential) = viewModelScope.launch {
        repository.deleteCredential(credential)
    }

    fun updateCredential(credential: Credential) = viewModelScope.launch {
        repository.updateCredential(credential)
    }
}
