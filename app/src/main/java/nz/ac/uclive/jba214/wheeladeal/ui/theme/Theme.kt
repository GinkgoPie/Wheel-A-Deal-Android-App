package nz.ac.uclive.jba214.wheeladeal.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import nz.ac.uclive.jba214.wheeladeal.R

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
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
fun WheelADealTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

val provider = GoogleFont.Provider (
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "",
    certificates = R.array.com_google_android_gms_fonts_certs

)

val fontName = GoogleFont("Quicksand")

val fontFamily = FontFamily(
    Font(
        googleFont = fontName,
        fontProvider = provider 
    )
)

    val replyTypography = Typography(
        titleLarge = TextStyle(
            fontFamily = fontFamily ,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = fontFamily ,
            fontWeight = FontWeight.Medium,
            lineHeight = 18.sp,
            letterSpacing = 0.sp
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily ,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 50.sp,
            letterSpacing = 0.sp
        ),

    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = replyTypography,
        content = content
    )
}