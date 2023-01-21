package com.kuluruvineeth.freeebooks

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.screens.MainScreen
import com.kuluruvineeth.freeebooks.ui.screens.NoInternetScreen
import com.kuluruvineeth.freeebooks.ui.theme.FreeEbooksTheme
import com.kuluruvineeth.freeebooks.ui.viewmodels.ThemeMode
import com.kuluruvineeth.freeebooks.ui.viewmodels.ThemeViewModel
import com.kuluruvineeth.freeebooks.utils.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var networkObserver: NetworkObserver
    lateinit var themeViewModel: ThemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceUtils.initialize(this)
        networkObserver = NetworkObserver(applicationContext)
        themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]

        when (PreferenceUtils.getInt(PreferenceUtils.APP_THEME, ThemeMode.Auto.ordinal)) {
            ThemeMode.Auto.ordinal -> themeViewModel.setTheme(ThemeMode.Auto)
            ThemeMode.Dark.ordinal -> themeViewModel.setTheme(ThemeMode.Dark)
            ThemeMode.Light.ordinal -> themeViewModel.setTheme(ThemeMode.Light)
        }

        themeViewModel.setMaterialYou(
            PreferenceUtils.getBoolean(
                PreferenceUtils.MATERIAL_YOU, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            )
        )

        setContent {
            FreeEbooksTheme(themeViewModel = themeViewModel) {

                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colorScheme.background, darkIcons = !isSystemInDarkTheme()
                )

                val status by networkObserver.observe().collectAsState(
                    initial = NetworkObserver.Status.Unavailable
                )
                MainScreen(status)
            }
        }
        checkStoragePermission()
    }

    fun checkStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d("MainActivity::Storage", "Permission is granted"); true
            } else {
                Log.d("MainActivity::Storage", "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this, arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE), 1
                ); false
            }
        } else {
            true
        }
    }
}