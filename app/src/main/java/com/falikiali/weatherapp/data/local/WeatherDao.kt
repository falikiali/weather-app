package com.falikiali.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addCurrentWeather(vararg weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weathers ORDER BY id DESC LIMIT 1")
    suspend fun getLatestWeather(): WeatherEntity?

    @Query("SELECT * FROM weathers ORDER BY id DESC")
    fun getCurrentLocationWeatherFromDb(): Flow<List<WeatherEntity>>

}