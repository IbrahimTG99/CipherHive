package com.devsinc.cipherhive.presentation.home

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

@Composable
fun HomeBottomMenu(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    val items = listOf(
        BottomMenuItems(Icons.Outlined.Home, "Home"),
        BottomMenuItems(Icons.Outlined.Person, "Profile"),
    )

    BottomNavigation(
        backgroundColor = Color(0xFFF1F1F1), modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp)).height(64.dp)
    ) {
        items.forEach {
            BottomNavigationItem(
                selected = false,
                onClick = {},
                alwaysShowLabel = false,
                label = { Text(text = it.title) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null,
                        tint = Color(0xFF545974),
                        modifier = Modifier.size(32.dp)
                    )
                })
        }
    }
}
