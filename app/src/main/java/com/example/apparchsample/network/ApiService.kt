package com.example.apparchsample.network

import com.example.apparchsample.network.dtos.NetworkVideoContainer
import com.example.apparchsample.network.dtos.RealEstate
import retrofit2.http.GET

interface ApiService {
    @GET("devbytes")
    suspend fun getPlaylist(): NetworkVideoContainer

    @GET("realestate")
    suspend fun getRealEstate(): List<RealEstate>
}