package com.theinsuranceboss.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.theinsuranceboss.app.R

val BossGold = Color(0xFFFAC000)
val BossBlack = Color(0xFF000000)
val BossDarkGray = Color(0xFF333333)
val BossLightGray = Color(0xFFD9D9D9)
val BossSurface = Color(0xFF1A1A1A)
val BossSurfaceHigh = Color(0xFF333333)
val BossOnSurface = Color(0xFFD9D9D9)
val BossMuted = Color(0xFFA3A3A3)
val BossBorder = Color(0xFF3F3F46)
val BossError = Color(0xFFF87171)
val BossSuccess = Color(0xFF4ADE80)

val Bitter = FontFamily(
    Font(R.font.bitter, FontWeight.Light),
    Font(R.font.bitter, FontWeight.Normal),
    Font(R.font.bitter, FontWeight.Medium),
    Font(R.font.bitter, FontWeight.SemiBold),
    Font(R.font.bitter, FontWeight.Bold),
    Font(R.font.bitter, FontWeight.ExtraBold),
    Font(R.font.bitter, FontWeight.Black),
)

val JetBrainsMono = FontFamily(
    Font(R.font.jetbrainsmono, FontWeight.Normal),
    Font(R.font.jetbrainsmono, FontWeight.Medium),
    Font(R.font.jetbrainsmono, FontWeight.SemiBold),
    Font(R.font.jetbrainsmono, FontWeight.Bold),
    Font(R.font.jetbrainsmono, FontWeight.ExtraBold),
)

val BossAccentFont = Bitter

private val BossColors = darkColorScheme(
    primary = BossGold,
    onPrimary = BossBlack,
    secondary = BossGold,
    onSecondary = BossBlack,
    background = BossBlack,
    onBackground = BossLightGray,
    surface = BossSurface,
    onSurface = BossLightGray,
    surfaceVariant = BossSurfaceHigh,
    onSurfaceVariant = BossMuted,
    outline = BossBorder,
    error = BossError,
    onError = BossBlack,
)

private val BossTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Black,
        fontSize = 42.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Bitter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.0.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.2.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = JetBrainsMono,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.4.sp,
    ),
)

@Composable
fun BossTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BossColors,
        typography = BossTypography,
        content = content,
    )
}
