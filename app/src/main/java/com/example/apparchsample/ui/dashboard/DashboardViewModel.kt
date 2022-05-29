package com.example.apparchsample.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.apparchsample.database.getDatabase
import com.example.apparchsample.repository.FunMarsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import kotlin.system.measureTimeMillis

class DashboardViewModel(application: Application) : AndroidViewModel(application) {
    private val funMarsRepository = FunMarsRepository(getDatabase(application))

    val playlist = funMarsRepository.videos

    val realEstateList= funMarsRepository.realEstate

    private var _eventNetworkError = MutableLiveData(false)
    private var _isNetworkErrorShown = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {
        viewModelScope.launch {
            try {
                val time = measureTimeMillis {
                    val refreshVideo = async { funMarsRepository.refreshVideos() }
                    val realEstate = async { funMarsRepository.getRealEstateData() }
                    Log.d("#DashboardVM","isCompleteVideo"+ refreshVideo.isCancelled.toString())
                    Log.d("#DashboardVM","isCompleteRealEstate"+ realEstate.isCompleted.toString())
                }
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false
                Log.d("#DashboardVM", "refresh repository")
                Log.d("#DashboardVM","request took $time ms ")

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if (playlist.value.isNullOrEmpty())
                    _eventNetworkError.value = true
                Log.d("#DashboardVM", networkError.toString())
            }
        }
    }

    private suspend fun delayTime(): String {
        delay(3000L)
        return "Show Concurrency"
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
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