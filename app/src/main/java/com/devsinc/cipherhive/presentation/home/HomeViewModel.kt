package com.devsinc.cipherhive.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.cipherhive.data.repository.CredentialRepository
import com.devsinc.cipherhive.domain.model.Credential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CredentialRepository
) : ViewModel() {

    fun getAllCredentials(): Flow<List<Credential>> {
        return repository.getAllCredentialsStream().stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
            )
    }
}
