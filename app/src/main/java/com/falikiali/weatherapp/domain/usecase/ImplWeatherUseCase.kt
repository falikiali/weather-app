package com.falikiali.weatherapp.domain.usecase

import com.falikiali.weatherapp.domain.model.Weather
import com.falikiali.weatherapp.domain.repository.WeatherRepository
import com.falikiali.weatherapp.helper.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImplWeatherUseCase @Inject constructor(private val weatherRepository: WeatherRepository): WeatherUseCase {

    /**
     * Remote
     */
    override suspend fun getCurrentLocationWeather(lat: Double, lon: Double): Flow<ResultState<String>> = weatherRepository.getCurrentLocationWeather(lat, lon)
    override suspend fun getWeatherByCity(): Flow<ResultState<List<Weather>>> = weatherRepository.getWeatherByCity()

    /**
     * Local
     */
    override fun getCurrentLocationFromDb(): Flow<List<Weather>> = weatherRepository.getCurrentLocationFromDb()

}