package com.example.apparchsample.ui.dashboard

import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.*
import com.example.apparchsample.database.getDatabase
import com.example.apparchsample.domain.PlansModel
import com.example.apparchsample.repository.FunMarsRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.net.URL

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val funMarsRepository = FunMarsRepository(getDatabase(application))
    private val _isDownloadFinished = MutableLiveData(false)
    private var _downloadCount = MutableLiveData(0)
    private var _isNetworkErrorShown = MutableLiveData(false)
    private var _totalFileSize = MutableLiveData(0)
    var wasDownloaded = false

    val _uiBindDataPlanList = MutableLiveData<List<PlansModel>>()

    val uiBindDataPlansList: LiveData<List<PlansModel>>
        get() = _uiBindDataPlanList

    val downloadCount: LiveData<Int>
        get() = _downloadCount

    val totalFileSize: LiveData<Int>
        get() = _totalFileSize

    val isDownloadFinished: LiveData<Boolean>
        get() = _isDownloadFinished

    val plansList = funMarsRepository.videos

    fun getProjects(token: String) {
        viewModelScope.launch {
            try {
                funMarsRepository.getProjects(token)
                _isNetworkErrorShown.value = false
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                Log.d("#DashboardVM", networkError.toString())
            }
        }
    }

    fun downloadImageFile(context: Context, plans: List<PlansModel>) {
        _totalFileSize.value = plans.size
        plans.forEach { value ->
            value.fileOriginalName?.let {
                downloadFile(context, it)
            }
        }
    }

    private fun downloadFile(context: Context, fileUrl: String) {
        val imagePathUrl = "https://qforb.sunrisedvp.systems/media/data/projects/52/plans/"
        val name = fileUrl.substringAfterLast("/")
        val url = URL(imagePathUrl + name)
        try {
            val folderName = "Sunrise"
            val fileLocation =
                "${Environment.getExternalStorageDirectory()}/${Environment.DIRECTORY_DOCUMENTS}/$folderName/"
            val direct = File(
                fileLocation
            )
            if (!direct.exists()) {
                direct.mkdirs()
                Log.d("fileCreate", "fileCreateSuccess")
            }
            val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val request =
                DownloadManager.Request(Uri.parse(url.toString()))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle(name)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOCUMENTS,
                    "/$folderName/" + File.separator + name
                )
            dm.enqueue(request)
        } catch (e: Exception) {
            Log.d("exception", e.message.toString())
        }
    }

    fun addDownloadCount() {
        _downloadCount.value = _downloadCount.value!! + 1
    }

    fun setDownloadFinished() {
        _isDownloadFinished.value = true
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}