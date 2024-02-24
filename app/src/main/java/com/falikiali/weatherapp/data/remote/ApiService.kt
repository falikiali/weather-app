package com.falikiali.weatherapp.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("weather")
    suspend fun getCurrentLocationWeather(@Query("lat") lat: Double, @Query("lon") lon: Double, @Query("appid") apiKey: String, @Query("units") units: String): Response<WeatherResponse>

    @GET("weather")
    suspend fun getWeatherByCity(@Query("q") city: String, @Query("appid") apiKey: String, @Query("units") units: String): Response<WeatherResponse>

}