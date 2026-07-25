package com.example.android_kyc_assignment.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val DarkGray = Color(0xFF1A1A1A)
val MediumGray = Color(0xFF333333)
val LightGray = Color(0xFFF5F5F5)
val OffWhite = Color(0xFFFAFAFA)
val AccentGray = Color(0xFF666666)
val BorderGray = Color(0xFFE0E0E0)
val SuccessGreen = Color(0xFF00C853)
val ErrorRed = Color(0xFFD32F2F)

@Composable
fun KycAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = White,
            onPrimary = Black,
            primaryContainer = DarkGray,
            secondary = LightGray,
            onSecondary = Black,
            background = Black,
            onBackground = White,
            surface = DarkGray,
            onSurface = White,
            surfaceVariant = MediumGray,
            onSurfaceVariant = LightGray,
            error = ErrorRed,
            onError = White
        )
    } else {
        lightColorScheme(
            primary = Black,
            onPrimary = White,
            primaryContainer = OffWhite,
            secondary = DarkGray,
            onSecondary = White,
            background = OffWhite,
            onBackground = Black,
            surface = White,
            onSurface = Black,
            surfaceVariant = LightGray,
            onSurfaceVariant = DarkGray,
            error = ErrorRed,
            onError = White
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as androidx.activity.ComponentActivity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineLarge = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            headlineMedium = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            ),
            titleLarge = MaterialTheme.typography.titleLarge.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
            ),
            titleMedium = MaterialTheme.typography.titleMedium.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
            ),
            bodyLarge = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Normal
            )
        ),
        shapes = Shapes(
            small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
            large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
        ),
        content = content
    )
}