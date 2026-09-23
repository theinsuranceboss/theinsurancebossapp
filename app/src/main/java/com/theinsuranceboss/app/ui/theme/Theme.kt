package com.theinsuranceboss.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.theinsuranceboss.app.R

val BossGold = Color(0xFFFAC000)
val BossBlack = Color(0xFF000000)
val BossSurface = Color(0xFF09090B)
val BossSurfaceHigh = Color(0xFF18181B)
val BossOnSurface = Color(0xFFF4F4F5)
val BossMuted = Color(0xFFA1A1AA)
val BossBorder = Color(0xFF27272A)
val BossError = Color(0xFFF87171)
val BossSuccess = Color(0xFF4ADE80)

val BitterFont = FontFamily(
    Font(R.font.bitter, FontWeight.Normal),
    Font(R.font.bitter, FontWeight.Medium),
    Font(R.font.bitter, FontWeight.SemiBold),
    Font(R.font.bitter, FontWeight.Bold),
    Font(R.font.bitter, FontWeight.ExtraBold),
    Font(R.font.bitter, FontWeight.Black),
    Font(R.font.bitter, FontWeight.Normal, FontStyle.Italic),
)

val MonoFont = FontFamily(
    Font(R.font.jetbrainsmono, FontWeight.Normal),
    Font(R.font.jetbrainsmono, FontWeight.Medium),
    Font(R.font.jetbrainsmono, FontWeight.Bold),
)

private val BossColors = darkColorScheme(
    primary = BossGold,
    onPrimary = BossBlack,
    secondary = BossGold,
    onSecondary = BossBlack,
    background = BossBlack,
    onBackground = BossOnSurface,
    surface = BossSurface,
    onSurface = BossOnSurface,
    surfaceVariant = BossSurfaceHigh,
    onSurfaceVariant = BossMuted,
    outline = BossBorder,
    error = BossError,
    onError = BossBlack,
)

private val BossTypography = Typography(
    displayLarge = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Black, fontSize = 40.sp, lineHeight = 46.sp),
    displayMedium = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.ExtraBold, fontSize = 32.sp, lineHeight = 38.sp),
    headlineLarge = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, lineHeight = 34.sp),
    headlineMedium = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 30.sp),
    headlineSmall = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Bold, fontSize = 20.sp, lineHeight = 26.sp),
    titleLarge = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 24.sp),
    titleMedium = TextStyle(fontFamily = MonoFont, fontWeight = FontWeight.Bold, fontSize = 14.sp, lineHeight = 20.sp, letterSpacing = 0.5.sp),
    titleSmall = TextStyle(fontFamily = MonoFont, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, letterSpacing = 0.8.sp),
    bodyLarge = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = BitterFont, fontWeight = FontWeight.Normal, fontSize = 12.sp, lineHeight = 16.sp),
    labelLarge = TextStyle(fontFamily = MonoFont, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 1.sp),
    labelMedium = TextStyle(fontFamily = MonoFont, fontWeight = FontWeight.Medium, fontSize = 12.sp, letterSpacing = 1.sp),
    labelSmall = TextStyle(fontFamily = MonoFont, fontWeight = FontWeight.Medium, fontSize = 10.sp, letterSpacing = 1.2.sp),
)

@Composable
fun BossTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BossColors,
        typography = BossTypography,
        content = content,
    )
}
