package com.kuluruvineeth.freeebooks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import com.kuluruvineeth.freeebooks.ui.viewmodels.ThemeMode
import com.kuluruvineeth.freeebooks.ui.viewmodels.SettingsViewModel

private val LightColors = lightColorScheme(
    onErrorContainer = md_theme_light_onErrorContainer,
    onError = md_theme_light_onError,
    errorContainer = md_theme_light_errorContainer,
    onTertiaryContainer = md_theme_light_onTertiaryContainer,
    onTertiary = md_theme_light_onTertiary,
    tertiaryContainer = md_theme_light_tertiaryContainer,
    tertiary = md_theme_light_tertiary,
    error = md_theme_light_error,
    outline = md_theme_light_outline,
    onBackground = md_theme_light_onBackground,
    background = md_theme_light_background,
    inverseOnSurface = md_theme_light_inverseOnSurface,
    inverseSurface = md_theme_light_inverseSurface,
    onSurfaceVariant = md_theme_light_onSurfaceVariant,
    onSurface = md_theme_light_onSurface,
    surfaceVariant = md_theme_light_surfaceVariant,
    surface = md_theme_light_surface,
    onSecondaryContainer = md_theme_light_onSecondaryContainer,
    onSecondary = md_theme_light_onSecondary,
    secondaryContainer = md_theme_light_secondaryContainer,
    secondary = md_theme_light_secondary,
    inversePrimary = md_theme_light_inversePrimary,
    onPrimaryContainer = md_theme_light_onPrimaryContainer,
    onPrimary = md_theme_light_onPrimary,
    primaryContainer = md_theme_light_primaryContainer,
    primary = md_theme_light_primary,
)


private val DarkColors = darkColorScheme(
    onErrorContainer = md_theme_dark_onErrorContainer,
    onError = md_theme_dark_onError,
    errorContainer = md_theme_dark_errorContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    tertiary = md_theme_dark_tertiary,
    error = md_theme_dark_error,
    outline = md_theme_dark_outline,
    onBackground = md_theme_dark_onBackground,
    background = md_theme_dark_background,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    surface = md_theme_dark_surface,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    secondary = md_theme_dark_secondary,
    inversePrimary = md_theme_dark_inversePrimary,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    primary = md_theme_dark_primary,
)

@Composable
fun FreeEbooksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    settingsViewModel: SettingsViewModel,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val themeState = settingsViewModel.theme.observeAsState(initial = ThemeMode.Auto)
    val materialYouState = settingsViewModel.materialYou.observeAsState(
        initial = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    )

    val colorScheme = when(themeState.value){
        ThemeMode.Light -> if(materialYouState.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            dynamicLightColorScheme(context)
        else LightColors
        ThemeMode.Dark -> if(materialYouState.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            dynamicDarkColorScheme(context)
        }
        else DarkColors
        ThemeMode.Auto -> if(materialYouState.value && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            if(darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }else{
            if(darkTheme) DarkColors else LightColors
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}