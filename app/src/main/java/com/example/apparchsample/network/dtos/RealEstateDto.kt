package com.example.apparchsample.network.dtos

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RealEstateDto(val realEstate: List<RealEstate>)

@JsonClass(generateAdapter = true)
data class RealEstate(
    val id: String,
    val type: String,
    val img_src: String
)

fun RealEstateDto.asDomainModel(): List<RealEstate> {
    return realEstate.map {
        RealEstate(
            id = it.id,
            type = it.type,
            img_src = it.img_src
        )
    }
}