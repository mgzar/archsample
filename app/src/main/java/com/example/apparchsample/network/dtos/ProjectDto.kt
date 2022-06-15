package com.example.apparchsample.network.dtos

import com.example.apparchsample.database.DatabaseVideo
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProjectsContainer(val items: List<Projects>)

@JsonClass(generateAdapter = true)
data class Projects(
    val id: String,
    val name: String?,
    val description: String?,
    val fileOriginalName: String,
)


fun ProjectsContainer.asDatabaseModel(): List<DatabaseVideo> {
    return items.map {
        DatabaseVideo(
            id = it.id,
            name = it.name,
            description = it.description,
            fileOriginalName = it.fileOriginalName
        )
    }
}