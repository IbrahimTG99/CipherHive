package com.devsinc.cipherhive.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.use_case.GetAllCredentials
import com.devsinc.cipherhive.domain.use_case.crypto.GetDecryptedPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllCredentialsFromRepo: GetAllCredentials,
    private val decryptedPassword: GetDecryptedPassword
) : ViewModel() {

    fun getAllCredentials(): Flow<List<Credential>> = getAllCredentialsFromRepo().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    fun decryptPassword(credential: Credential): String = decryptedPassword(credential)
}
