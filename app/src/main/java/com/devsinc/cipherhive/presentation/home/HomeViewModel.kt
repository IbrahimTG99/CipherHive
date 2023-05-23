package com.devsinc.cipherhive.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.cipherhive.data.repository.CredentialRepository
import com.devsinc.cipherhive.domain.model.Credential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CredentialRepository
): ViewModel() {

    fun insertCredential(credential: Credential) = viewModelScope.launch {
        repository.insertCredential(credential)
    }

    fun deleteCredential(credential: Credential) = viewModelScope.launch {
        repository.deleteCredential(credential)
    }

    fun updateCredential(credential: Credential) = viewModelScope.launch {
        repository.updateCredential(credential)
    }

    fun getAllCredentials(): Flow<List<Credential>> {
        return repository.getAllCredentialsStream()
    }

    fun getCredentialsByUrl(url: String): Flow<List<Credential>> {
        return repository.getCredentialsByUrlStream(url)
    }
}
