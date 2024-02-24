package com.falikiali.weatherapp.domain.repository

import com.falikiali.weatherapp.domain.model.Weather
import com.falikiali.weatherapp.helper.ResultState
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    /**
     * Remote
     */
    suspend fun getCurrentLocationWeather(lat: Double, lon: Double): Flow<ResultState<String>>
    suspend fun getWeatherByCity(): Flow<ResultState<List<Weather>>>

    /**
     * Local
     */
    fun getCurrentLocationFromDb(): Flow<List<Weather>>

}