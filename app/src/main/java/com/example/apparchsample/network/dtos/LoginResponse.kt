package com.example.apparchsample.network.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val user: User
)

@JsonClass(generateAdapter = true)
data class User(
    val name: String,
    val email: String,
    val phone: String,
    val token: String,
    val deviceToken:String
)

fun LoginResponse.asDomainModel(): User {
    return user

}