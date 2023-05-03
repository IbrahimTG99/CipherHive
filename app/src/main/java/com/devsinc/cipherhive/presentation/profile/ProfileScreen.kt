package com.devsinc.cipherhive.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devsinc.cipherhive.CryptoManager
import com.devsinc.cipherhive.presentation.sign_in.UserData
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun ProfileScreen(
    userData: UserData?, onSignOut: () -> Unit, cryptoManager: CryptoManager, filesDir: File
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
    }
    // demo for testing encryption
    var messageToEncrypt by remember { mutableStateOf("") }

    var messageToDecrypt by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = messageToEncrypt,
            onValueChange = { messageToEncrypt = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Enter message to encrypt") })
        Spacer(modifier = Modifier.size(8.dp))
        Row {
            Button(onClick = {
                val bytes = messageToEncrypt.encodeToByteArray()
                val file = File(filesDir, "test.txt")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val fos = FileOutputStream(file)
                messageToDecrypt = cryptoManager.encrypt(
                    data = bytes,
                    outputStream = fos
                ).decodeToString()

            }) {
                Text(text = "Encrypt")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val file = File(filesDir, "test.txt")
                messageToEncrypt = cryptoManager.decrypt(
                    inputStream = FileInputStream(file)
                ).decodeToString()
            }) {
                Text(text = "Decrypt")
            }
        }
        Text(text = messageToDecrypt)
    }
}
