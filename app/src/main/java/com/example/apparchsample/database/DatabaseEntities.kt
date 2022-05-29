package com.example.apparchsample.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.apparchsample.domain.SampleVideo

@Entity
data class DatabaseVideo constructor(
        @PrimaryKey
        val url: String,
        val updated: String,
        val title: String,
        val description: String,
        val thumbnail: String)

fun List<DatabaseVideo>.asDomainModel(): List<SampleVideo> {
        return map {
                SampleVideo(
                        url = it.url,
                        title = it.title,
                        description = it.description,
                        updated = it.updated,
                        thumbnail = it.thumbnail)
        }
}
