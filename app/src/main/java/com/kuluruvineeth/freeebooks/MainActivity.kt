package com.kuluruvineeth.freeebooks

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kuluruvineeth.freeebooks.others.NetworkObserver
import com.kuluruvineeth.freeebooks.ui.screens.MainScreen
import com.kuluruvineeth.freeebooks.ui.theme.FreeEbooksTheme
import com.kuluruvineeth.freeebooks.ui.viewmodels.ThemeMode
import com.kuluruvineeth.freeebooks.ui.viewmodels.SettingsViewModel
import com.kuluruvineeth.freeebooks.utils.PreferenceUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var networkObserver: NetworkObserver
    lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceUtils.initialize(this)
        networkObserver = NetworkObserver(applicationContext)
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        when (PreferenceUtils.getInt(PreferenceUtils.APP_THEME, ThemeMode.Auto.ordinal)) {
            ThemeMode.Auto.ordinal -> settingsViewModel.setTheme(ThemeMode.Auto)
            ThemeMode.Dark.ordinal -> settingsViewModel.setTheme(ThemeMode.Dark)
            ThemeMode.Light.ordinal -> settingsViewModel.setTheme(ThemeMode.Light)
        }

        settingsViewModel.setMaterialYou(
            PreferenceUtils.getBoolean(
                PreferenceUtils.MATERIAL_YOU, Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
            )
        )

        setContent {
            FreeEbooksTheme(settingsViewModel = settingsViewModel) {

                val systemUiController = rememberSystemUiController()
                systemUiController.setSystemBarsColor(
                    color = MaterialTheme.colorScheme.background, darkIcons = !isSystemInDarkTheme()
                )

                val status by networkObserver.observe().collectAsState(
                    initial = NetworkObserver.Status.Unavailable
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(networkStatus = status)
                }
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