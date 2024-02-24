package com.falikiali.weatherapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.falikiali.weatherapp.domain.model.Weather

@Entity(tableName = "weathers")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo("city") val city: String,
    @ColumnInfo("temp") val temp: Int,
    @ColumnInfo("max_temp") val maxTemp: Int,
    @ColumnInfo("min_temp") val minTemp: Int,
    @ColumnInfo("date") val date: Long,
    @ColumnInfo("weather") val weather: String,
    @ColumnInfo("wind") val wind: Float,
    @ColumnInfo("humidity") val humidity: Int,
    @ColumnInfo("pressure") val pressure: Int
) {
    fun toDomain(): Weather {
        return Weather(
            id!!,
            city,
            temp,
            maxTemp,
            minTemp,
            date,
            weather,
            wind,
            humidity,
            pressure
        )
    }
}
