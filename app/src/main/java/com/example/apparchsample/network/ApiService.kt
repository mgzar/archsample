package com.example.apparchsample.network

import com.example.apparchsample.network.dtos.LoginResponse
import com.example.apparchsample.network.dtos.ProjectsContainer
import retrofit2.http.*

interface ApiService {
    @GET("devbytes")
    suspend fun getPlaylist(): ProjectsContainer

    @GET("{token}/projects/plans/52")
    suspend fun getProjects(@Path("token") token: String): ProjectsContainer

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse

}