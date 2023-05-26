package com.devsinc.cipherhive.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.devsinc.cipherhive.presentation.sign_in.UserData
import com.devsinc.cipherhive.ui.theme.BebasNue
import com.devsinc.cipherhive.ui.theme.Poppins

@Composable
fun ProfileScreen(
    userData: UserData?,
    onSignOut: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp),
                fontFamily = BebasNue(),
                color = Color(0xFF545974),
                fontSize = 20.sp
            )
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userData?.profilePictureUrl != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape).border(
                            width = 2.dp,
                            color = Color(0xFFFF6464),
                            shape = CircleShape
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = userData?.username ?: "",
                color = Color(0xFF545974),
                fontSize = 16.sp,
                fontFamily = Poppins(),
                fontWeight = FontWeight.SemiBold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .padding(it),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6464)
                )
            ) {
                Text(text = "Sign Out")
            }
        }
    }
}
