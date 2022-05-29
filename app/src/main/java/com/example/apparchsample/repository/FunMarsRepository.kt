package com.example.apparchsample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.apparchsample.database.FunMarsDatabase
import com.example.apparchsample.database.asDomainModel
import com.example.apparchsample.domain.SampleVideo
import com.example.apparchsample.network.NetworkService
import com.example.apparchsample.network.dtos.RealEstate
import com.example.apparchsample.network.dtos.asDatabaseModel
import com.example.apparchsample.network.dtos.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FunMarsRepository(private val database: FunMarsDatabase) {
    private val _realEstateList = MutableLiveData<List<RealEstate>>()

    val videos: LiveData<List<SampleVideo>> = Transformations.map(database.funMarsDao.getVideos()) {
        it.asDomainModel()
    }

    val realEstate: LiveData<List<RealEstate>>
        get() = _realEstateList

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = NetworkService.apiService.getPlaylist()
            database.funMarsDao.insertAll(playlist.asDatabaseModel())
        }
    }

    suspend fun getRealEstateData() {
        withContext(Dispatchers.IO) {
            val dataList = NetworkService.apiService.getRealEstate()
            _realEstateList.postValue( dataList)
        }
    }

}