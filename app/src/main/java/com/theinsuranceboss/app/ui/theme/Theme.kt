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

val LeagueSpartan = FontFamily(
    Font(R.font.league_spartan, FontWeight.Normal),
    Font(R.font.league_spartan, FontWeight.Medium),
    Font(R.font.league_spartan, FontWeight.SemiBold),
    Font(R.font.league_spartan, FontWeight.Bold),
    Font(R.font.league_spartan, FontWeight.ExtraBold),
    Font(R.font.league_spartan, FontWeight.Black),
)

val Montserrat = FontFamily(
    Font(R.font.montserrat, FontWeight.Normal),
    Font(R.font.montserrat, FontWeight.Medium),
    Font(R.font.montserrat, FontWeight.SemiBold),
    Font(R.font.montserrat, FontWeight.Bold),
    Font(R.font.montserrat, FontWeight.ExtraBold),
    Font(R.font.montserrat, FontWeight.Black),
)

val SignatureScript = FontFamily(
    Font(R.font.great_vibes, FontWeight.Normal),
)

val BossAccentFont = SignatureScript

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
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Black,
        fontSize = 42.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.3).sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.2).sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.4.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.6.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 18.sp,
        letterSpacing = 1.2.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.4.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = LeagueSpartan,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.6.sp,
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
