package com.example.apparchsample.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FunMarsDao {
    @Query("select * from databasevideo")
    fun getVideos(): LiveData<List<DatabaseVideo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabaseVideo>)

}



@Database(entities = [DatabaseVideo::class], version = 1)
abstract class FunMarsDatabase: RoomDatabase() {
    abstract val funMarsDao: FunMarsDao
}

private lateinit var INSTANCE: FunMarsDatabase

fun getDatabase(context: Context): FunMarsDatabase {
    synchronized(FunMarsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                FunMarsDatabase::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}