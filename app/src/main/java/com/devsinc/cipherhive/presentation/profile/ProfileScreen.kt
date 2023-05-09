package com.devsinc.cipherhive.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import coil.compose.AsyncImage
import com.devsinc.cipherhive.model.UserDatum
import com.devsinc.cipherhive.presentation.sign_in.UserData
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit,
    dataStore: DataStore<UserDatum>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.size(16.dp))
        if (userData?.profilePictureUrl != null) {
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(128.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        if (userData?.username != null) {
            Text(
                text = userData.username,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.size(16.dp))
        }
        Button(onClick = onSignOut) {
            Text(text = "Sign Out")
        }

        Spacer(modifier = Modifier.size(16.dp))

        // demo for testing encryption
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var readData by remember { mutableStateOf(UserDatum()) }

        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            TextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(onClick = {
                    scope.launch {
                        dataStore.updateData {
                            UserDatum(
                                username = username, password = password
                            )
                        }
                    }
                }) {
                    Text(text = "Save")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    scope.launch {
                        readData = dataStore.data.first()
                    }
                }) {
                    Text(text = "Read")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Read data: $readData")
        }
    }
}
