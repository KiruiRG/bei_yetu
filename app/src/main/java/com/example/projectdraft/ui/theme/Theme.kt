package com.example.projectdraft.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF601EF9),
    onPrimary = Color.White,//This will be used for text if the background is the primary color
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ProjectdraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    /*Above, you have to always set the dynamicColor to false. The default is true. When dynamicColor = true, Android replaces
    your LightColorScheme with Material You colors generated from the user’s wallpaper. What this means is that it ignores the colors
    you have specified and chooses colors from the number of colors in the users wallpaper. For an emulator, it takes the colors
    from the emulator's wallpaper(you can see it before the app is displayed). So you need to always set it to false or just
    remove it entirely. I'm not removing it entirely here cz I want a future reference*/
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val customTypography = Typography(
        titleLarge = TextStyle(
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
            /*For font weight, you can choose the predefined which include:
            * FontWeight.Thin -> 100
            * FontWeight.Light -> 300
            * FontWeight.Normal -> 400
            * FontWeight.Medium -> 500
            * FontWeight.Semibold -> 600
            * FontWeight.Bold -> 700
            * FontWeight.Extrabold -> 800
            * FontWeight.Black -> 900
            * OR
            * You can define for urself like this: fontWeight = FontWeight(450)*/
        ),

        titleMedium = TextStyle(
            fontSize = 35.sp,
            fontWeight = FontWeight.SemiBold
        ),

        titleSmall = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = customTypography,
        content = content
    )

 }