package com.devsinc.cipherhive.domain.service

import android.app.assist.AssistStructure
import android.content.Intent
import android.net.Uri
import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveInfo
import android.service.autofill.SaveRequest
import android.text.InputType
import android.util.Log
import android.view.autofill.AutofillId
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.devsinc.cipherhive.R
import com.devsinc.cipherhive.domain.model.Credential
import com.devsinc.cipherhive.domain.repository.CredentialRepository
import com.devsinc.cipherhive.util.CryptoManager
import com.devsinc.cipherhive.util.PasswordGenerator
import com.devsinc.cipherhive.util.Util.json
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import java.math.BigInteger
import java.security.MessageDigest
import javax.inject.Inject

@AndroidEntryPoint
class AutoFillService : AutofillService() {
    private val coroutineScope = CoroutineScope(context = Dispatchers.Unconfined)

    private var usernameHints = arrayOf<String>()
    private var passwordHints = arrayOf<String>()

    private var ready = false
    private var idPackage = ""
    private var viewWebDomain = ""
    private var saveUsername = ""
    private var savePassword = ""
    private var usernameId = mutableListOf<AutofillId>()
    private var passwordId = mutableListOf<AutofillId>()
    private var fillResponse = FillResponse.Builder()

    @Inject
    lateinit var credentialRepository: CredentialRepository


    override fun onCreate() {
        super.onCreate()
        usernameHints = resources.getStringArray(R.array.username_hints)
        passwordHints = resources.getStringArray(R.array.password_hints)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onFillRequest(
        request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback
    ) {
        coroutineScope.launch {
            ready = false
            idPackage = ""
            viewWebDomain = ""
            saveUsername = ""
            savePassword = ""
            usernameId = mutableListOf()
            passwordId = mutableListOf()
            fillResponse = FillResponse.Builder()

            val context = request.fillContexts
            val structure = context.last().structure

            traverseStructure(structure = structure, mode = false)

            if (usernameId.isNotEmpty() && passwordId.isNotEmpty()) {
                fillResponse.setSaveInfo(
                    SaveInfo.Builder(
                        SaveInfo.SAVE_DATA_TYPE_USERNAME or SaveInfo.SAVE_DATA_TYPE_PASSWORD,
                        arrayOf(usernameId.last(), passwordId.last())
                    ).build()
                )
            }

            if (passwordId.isNotEmpty()) {
                val randomPassword = async { PasswordGenerator().generatePassword() }

                passwordId.forEach {
                    val credentialsPresentation =
                        RemoteViews(packageName, R.layout.autofill_list_item)
                    credentialsPresentation.setTextViewText(
                        R.id.label, getString(R.string.random_password)
                    )
                    credentialsPresentation.setTextViewText(
                        R.id.username, getString(R.string.autofill_random_password)
                    )
                    credentialsPresentation.setImageViewBitmap(
                        R.id.favicon,
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_app_logo, null)
                            ?.toBitmap()
                    )

                    try {
                        fillResponse.addDataset(
                            Dataset.Builder().setValue(
                                it,
                                AutofillValue.forText(randomPassword.await()),
                                credentialsPresentation
                            ).build()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                try {
                    callback.onSuccess(fillResponse.build())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {
        idPackage = ""
        viewWebDomain = ""

        saveUsername = ""
        savePassword = ""

        fillResponse = FillResponse.Builder()
        usernameId = mutableListOf()
        passwordId = mutableListOf()
        ready = false

        val context = request.fillContexts
        val structure = context.last().structure

        traverseStructure(structure = structure, mode = true)

        if (saveUsername.isNotEmpty() && savePassword.isNotEmpty()) {
            var hash = BigInteger(
                1, MessageDigest.getInstance("SHA-1").digest(savePassword.toByteArray())
            ).toString(16)
            while (hash.length < 32) hash = "0$hash"

            val appName = idPackage.substringAfter(delimiter = ".").substringBefore(delimiter = ".")

            val params = mutableMapOf<String, String>("password" to savePassword,
                "label" to when {
                    viewWebDomain.isNotEmpty() -> viewWebDomain.removePrefix(prefix = "www.")
                        .substringBefore(delimiter = ".").replaceFirstChar { it.titlecase() }

                    idPackage.isNotEmpty() -> {
                        try {
                            packageManager.getApplicationLabel(
                                packageManager.getApplicationInfo(
                                    idPackage, 0
                                )
                            ).toString()
                        } catch (e: Exception) {
                            appName.replaceFirstChar { it.titlecase() }
                        }
                    }

                    else -> "Unknown"
                },
                "username" to saveUsername,
                "url" to viewWebDomain.ifEmpty { "$appName.com" },
                "hash" to hash
            )

            if (viewWebDomain.isEmpty()) {
                params["customFields"] = json.encodeToString(
                    value = listOf(
                        mapOf(
                            "label" to "Android app", "type" to "text", "value" to idPackage
                        )
                    )
                )
            }

            Log.d("AutoFillService", "onSaveRequest: $params")
            coroutineScope.launch {
                try {
                    // will save password to database
                    Log.d("AutoFillService", "onSaveRequest: $params")
                    callback.onSuccess()
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback.onFailure(e.message)
                }
            }
        }
    }

    private fun traverseStructure(structure: AssistStructure, mode: Boolean) {
        val windowNodes = structure.run { (0 until windowNodeCount).map { getWindowNodeAt(it) } }

        windowNodes.forEach { traverseNode(viewNode = it.rootViewNode, mode = mode) }
    }

    private fun traverseNode(viewNode: AssistStructure.ViewNode, mode: Boolean) {
        if (viewNode.webDomain != null && viewWebDomain.isEmpty()) viewWebDomain =
            viewNode.webDomain!!
        if (viewNode.idPackage?.contains(".") == true) idPackage = viewNode.idPackage.toString()

        if (!mode) {
            Log.d("AutoFillService", "traverseNode: $usernameId, $passwordId, $ready")
            if (usernameId.isNotEmpty() && passwordId.isNotEmpty() && !ready) {
                coroutineScope.launch {
                    try {
                        credentialRepository.getAllCredentialsStream().collect { credentials ->
                            credentials.forEach { password ->
                                if (checkSuggestions(password = password)) {
                                    val cryptoManager =
                                        CryptoManager("CH_${password.label}-${password.username}_KEY")
                                    val credentialsPresentation =
                                        RemoteViews(packageName, R.layout.autofill_list_item)
                                    credentialsPresentation.setTextViewText(
                                        R.id.label, password.label
                                    )
                                    credentialsPresentation.setTextViewText(
                                        R.id.username, password.username
                                    )
                                    credentialsPresentation.setImageViewBitmap(
                                        R.id.favicon,
                                        password.favicon ?: ResourcesCompat.getDrawable(
                                            resources, R.drawable.ic_app_logo, null
                                        )?.toBitmap()
                                    )

                                    fillResponse.addDataset(
                                        Dataset.Builder().setValue(
                                            usernameId.last(),
                                            AutofillValue.forText(password.username),
                                            credentialsPresentation
                                        ).setValue(
                                            passwordId.last(),
                                            AutofillValue.forText(cryptoManager.decrypt(password.password)),
                                            credentialsPresentation
                                        ).build()
                                    )

                                    ready = true
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }

            if (checkUsernameHints(viewNode = viewNode) && !usernameId.contains(element = viewNode.autofillId)) {
                usernameId.add(element = viewNode.autofillId!!)

                if (usernameId.size == passwordId.size) ready = false
            } else if (checkPasswordHints(viewNode = viewNode) && !passwordId.contains(element = viewNode.autofillId)) {
                passwordId.add(element = viewNode.autofillId!!)

                if (passwordId.size < usernameId.size) ready = false
            } else fillResponse.setIgnoredIds(viewNode.autofillId)
        } else {
            if (checkUsernameHints(viewNode = viewNode) && viewNode.text?.isNotEmpty() == true) saveUsername =
                viewNode.text.toString()
            else if (checkPasswordHints(viewNode = viewNode) && viewNode.text?.isNotEmpty() == true) savePassword =
                viewNode.text.toString()
            else fillResponse.setIgnoredIds(viewNode.autofillId)
        }

        if (usernameId.isEmpty() || passwordId.isEmpty()) {
            val children = viewNode.run { (0 until childCount).map { getChildAt(it) } }
            children.forEach { childNode -> traverseNode(viewNode = childNode, mode = mode) }
        }
    }

    private fun checkUsernameHints(viewNode: AssistStructure.ViewNode): Boolean {
        return usernameHints.any { hint ->
            viewNode.autofillHints?.any {
                it.contains(other = hint, ignoreCase = true) || hint.contains(
                    other = it,
                    ignoreCase = true
                )
            } == true || viewNode.hint?.contains(
                other = hint,
                ignoreCase = true
            ) == true || (viewNode.hint?.isNotEmpty() == true && hint.contains(
                other = viewNode.hint.toString(),
                ignoreCase = true
            ))
        }
    }

    private fun checkPasswordHints(viewNode: AssistStructure.ViewNode): Boolean {
        return passwordHints.any { hint ->
            viewNode.autofillHints?.any {
                it.contains(other = hint, ignoreCase = true) || hint.contains(
                    other = it,
                    ignoreCase = true
                )
            } == true || viewNode.hint?.contains(
                other = hint,
                ignoreCase = true
            ) == true || hint.contains(other = viewNode.hint.toString(), ignoreCase = true)
        } && (viewNode.inputType and InputType.TYPE_TEXT_VARIATION_PASSWORD == InputType.TYPE_TEXT_VARIATION_PASSWORD || viewNode.inputType and InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD || viewNode.inputType and InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD || viewNode.inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD == InputType.TYPE_NUMBER_VARIATION_PASSWORD || viewNode.inputType and InputType.TYPE_DATETIME_VARIATION_NORMAL == InputType.TYPE_DATETIME_VARIATION_NORMAL) // this is necessary for autofill to work on Amazon's apps
    }

    private fun checkSuggestions(password: Credential): Boolean {
        val domain = viewWebDomain.removePrefix(prefix = "www.")

        return ((domain.isNotEmpty() && (password.url.contains(
            other = domain,
            ignoreCase = true
        ) || domain.contains(
            other = password.label,
            ignoreCase = true
        ) || domain.contains(other = password.url, ignoreCase = true) || password.url.contains(
            other = domain.substringBefore(delimiter = "."), ignoreCase = true
        ) || domain.substringBefore(delimiter = ".").contains(
            other = password.label, ignoreCase = true
        ))) || (domain.isEmpty() && idPackage.isNotEmpty() && (idPackage.contains(
            other = password.label, ignoreCase = true
        ) || try {
            idPackage.contains(
                other = Uri.parse(password.url).host!!, ignoreCase = true
            )
        } catch (e: Exception) {
            false
        } || password.packageId.equals(other = idPackage, ignoreCase = true))))
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
