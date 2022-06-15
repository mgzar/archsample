package com.example.apparchsample.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.apparchsample.database.FunMarsDatabase
import com.example.apparchsample.database.asDomainModel
import com.example.apparchsample.domain.PlansModel
import com.example.apparchsample.network.NetworkService
import com.example.apparchsample.network.dtos.LoginResponse
import com.example.apparchsample.network.dtos.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FunMarsRepository(private val database: FunMarsDatabase) {
    private val _loginResponse = MutableLiveData<LoginResponse>()

    val videos: LiveData<List<PlansModel>> = Transformations.map(database.funMarsDao.getVideos()) {
        it.asDomainModel()
    }

    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    suspend fun getProjects(token: String) {
        withContext(Dispatchers.IO) {
            val plansList = NetworkService.apiService.getProjects(token)
            database.funMarsDao.insertAll(plansList.asDatabaseModel())
        }
    }

    suspend fun setLogin(username: String, password: String) {
        withContext(Dispatchers.IO) {
            val responseBody = NetworkService.apiService.postLogin(username, password)
            _loginResponse.postValue(responseBody)
        }
    }

}