package com.devsinc.cipherhive.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.devsinc.cipherhive.ui.theme.BebasNue
import com.devsinc.cipherhive.ui.theme.Poppins

@Composable
fun ProfileScreen(
    navController: NavController
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Password,
                    contentDescription = null,
                    modifier = Modifier.padding(start = 24.dp, top = 20.dp),
                    tint = Color(0xFFFF6464)
                )
                Text(
                    text = "Profile",
                    modifier = Modifier
                        .padding(start = 20.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = BebasNue(),
                    color = Color(0xFF545974),
                    fontSize = 20.sp
                )
            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Password,
                contentDescription = "App Icon",
                modifier = Modifier.size(128.dp),
                tint = Color(0xFFFF6464)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = "CipherHive",
                color = Color(0xFF545974),
                fontSize = 32.sp,
                fontFamily = BebasNue(),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Your secure password manager",
                color = Color(0xFFBABABA),
                fontSize = 14.sp,
                fontFamily = Poppins(),
                textAlign = TextAlign.Center
            )
        }
    }
}
