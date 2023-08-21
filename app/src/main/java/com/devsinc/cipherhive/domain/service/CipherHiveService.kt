package com.devsinc.cipherhive.domain.service

import android.content.Intent
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.view.autofill.AutofillId
import com.devsinc.cipherhive.R
import com.devsinc.cipherhive.domain.repository.CredentialRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CipherHiveService : AutofillService() {

    private var usernameHints = arrayOf<String>()
    private var passwordHints = arrayOf<String>()

    @Inject
    lateinit var credentialRepository: CredentialRepository

    private var usernameId = mutableListOf<AutofillId>()
    private var passwordId = mutableListOf<AutofillId>()
    private var fillResponse = FillResponse.Builder()

    private val coroutineScope = CoroutineScope(context = Dispatchers.Unconfined)

    override fun onConnected() {
        super.onConnected()
        usernameHints = resources.getStringArray(R.array.username_hints)
        passwordHints = resources.getStringArray(R.array.password_hints)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onFillRequest(
        request: FillRequest,
        cancellationSignal: CancellationSignal,
        callback: FillCallback
    ) {
        TODO("Not yet implemented")
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        TODO("Not yet implemented")
    }
}
