package com.example.apparchsample.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


object NetworkService {
    private const val  baseUrl = "https://qforb.sunrisedvp.systems/api/json/"
    private const val apiVersion ="v1/"
    private var interceptor =HttpLoggingInterceptor()

    private val defaultUr ="https://android-kotlin-fun-mars-server.appspot.com/"
//\$baseUrl$apiVersion
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl+ apiVersion)
        .client(httpClient())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)

    private fun httpClient(): OkHttpClient {
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

}


