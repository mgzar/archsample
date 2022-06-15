package com.example.apparchsample.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apparchsample.domain.PlansModel

@Entity
data class DatabaseVideo(
    @PrimaryKey
    val id: String,
    val name: String?,
    val description: String?,
    val fileOriginalName: String?
)

fun List<DatabaseVideo>.asDomainModel(): List<PlansModel> {
    return map {
        PlansModel(
            id = it.id,
            name = it.name,
            description = it.description,
            fileOriginalName = it.fileOriginalName,
        )
    }
}
