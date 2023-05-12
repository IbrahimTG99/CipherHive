package com.devsinc.cipherhive.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devsinc.cipherhive.R

// Set of Material typography styles to start with
val Typography = androidx.compose.material3.Typography(
    displayLarge = TextStyle(
        fontFamily = BebasNue(),
        fontWeight = FontWeight.Normal,
        fontSize = 64.sp,
        color = Color(0xFFFF6464)
    )
)


fun Poppins() = FontFamily(
    Font(R.font.font_poppins_light, FontWeight.Light),
    Font(R.font.font_poppins_regular, FontWeight.Normal),
    Font(R.font.font_poppins_semibold, FontWeight.SemiBold),
)


fun BebasNue() = FontFamily(
    Font(R.font.font_bebas_neue, FontWeight.Normal)
)
