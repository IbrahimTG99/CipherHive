package com.devsinc.cipherhive.presentation.credential

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCredential(
    bottomSheetState: SheetState, scope: CoroutineScope, openBottomSheet: MutableState<Boolean>
) {
    val viewModel: CredentialViewModel = hiltViewModel()
    val state = viewModel.state
    val context = LocalContext.current
    var passwordHidden by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is CredentialViewModel.ValidationEvent.Success -> {
                    bottomSheetState.hide()
                    openBottomSheet.value = false
                    Toast.makeText(context, "Credential saved successfully", Toast.LENGTH_SHORT)
                        .show()
                }

                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(value = state.label,
            onValueChange = {
                viewModel.onEvent(CredentialFormEvent.OnLabelChanged(it))
            },
            isError = state.labelError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "Label*") },
            placeholder = { Text(text = "Label") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
        if (state.labelError != null) {
            Text(
                text = state.labelError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(value = state.username,
            onValueChange = {
                viewModel.onEvent(CredentialFormEvent.OnUsernameChanged(it))
            },
            isError = state.usernameError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "Username*") },
            placeholder = { Text(text = "Username") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
        if (state.usernameError != null) {
            Text(
                text = state.usernameError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(value = state.password,
            onValueChange = {
                viewModel.onEvent(CredentialFormEvent.OnPasswordChanged(it))
            },
            isError = state.passwordError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "Password*") },
            placeholder = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            })
        if (state.passwordError != null) {
            Text(
                text = state.passwordError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(value = state.url,
            onValueChange = {
                viewModel.onEvent(CredentialFormEvent.OnUrlChanged(it))
            },
            isError = state.urlError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = { Text(text = "URL*") },
            placeholder = { Text(text = "URL") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri
            )
        )
        if (state.urlError != null) {
            Text(
                text = state.urlError,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.labelMedium
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))

        OutlinedTextField(value = state.notes,
            onValueChange = {
                viewModel.onEvent(CredentialFormEvent.OnNotesChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = "Notes") },
            placeholder = { Text(text = "Notes") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )
        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            onClick = {
                viewModel.onEvent(CredentialFormEvent.OnSaveClicked)
            }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF6464)
            )
        ) {
            Text(text = "Save")
        }

    }
}
