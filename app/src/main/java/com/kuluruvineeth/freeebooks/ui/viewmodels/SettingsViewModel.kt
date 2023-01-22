package com.kuluruvineeth.freeebooks.ui.viewmodels

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuluruvineeth.freeebooks.BuildConfig
import com.kuluruvineeth.freeebooks.MainActivity
import com.kuluruvineeth.freeebooks.R
import com.kuluruvineeth.freeebooks.utils.toToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

enum class ThemeMode{
    Light, Dark, Auto
}


class SettingsViewModel : ViewModel() {

    private val okHttpClient = OkHttpClient()

    companion object{
        private const val GITHUB_RELEASES_LINK = "https://api.github.com/repos/kuluruvineeth/FreeEbooks/releases"
    }


    private val _theme = MutableLiveData(ThemeMode.Auto)
    private val _materialYou = MutableLiveData(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)

    val theme: LiveData<ThemeMode> = _theme
    val materialYou: LiveData<Boolean> = _materialYou

    fun setTheme(newTheme: ThemeMode){
        _theme.postValue(newTheme)
    }

    fun setMaterialYou(newValue: Boolean){
        _materialYou.postValue(newValue)
    }

    @Composable
    fun getCurrentTheme() : ThemeMode {
        return if(theme.value == ThemeMode.Auto){
            if(isSystemInDarkTheme()) ThemeMode.Dark else ThemeMode.Light
        }else theme.value!!
    }

    fun checkForUpdates(onResult: (isUpdateAvailable: Boolean, newReleaseLink: String?, errorOnRequest: Boolean) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            val request = Request.Builder().get().url(GITHUB_RELEASES_LINK).build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    onResult(false,null,true)
                }

                override fun onResponse(call: Call, response: Response) {
                    val versionRegex = Regex("[0-9]+\\.[0-9]+\\.[0-9]+")
                    val jsonObj = JSONArray(response.body!!.string())
                    val latestRelJson = jsonObj.getJSONObject(0)
                    val tagName = latestRelJson.getString("tag_name")
                    val latestVersion = versionRegex.find(BuildConfig.VERSION_NAME)?.value
                    val appVersion = versionRegex.find(BuildConfig.VERSION_NAME)?.value
                    if(appVersion != latestVersion){
                        val latestRelAssets = latestRelJson.getJSONArray("assets")
                        val latestReleaseLink = latestRelAssets.getJSONObject(0).getString("browser_download_url")
                        onResult(true,latestReleaseLink,false)
                    }else{
                        onResult(false,null,false)
                    }
                }
            })
        }
    }

    fun downloadUpdate(downloadUrl: String, activity: MainActivity){
        if(activity.checkStoragePermission()){
            val fileName = downloadUrl.split("/").last()
            val manager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(downloadUrl)
            val request = DownloadManager.Request(uri)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setAllowedOverRoaming(true)
                .setAllowedOverMetered(true)
                .setTitle(activity.getString(R.string.downloading_update))
                .setDescription(fileName)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
            manager.enqueue(request)
            activity.getString(R.string.downloading_update).toToast(activity)
        }else{
            activity.getString(R.string.storage_perm_error).toToast(activity)
        }
    }
}