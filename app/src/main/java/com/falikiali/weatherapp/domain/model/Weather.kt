package com.falikiali.weatherapp.domain.model

data class Weather(
    val id: Int,
    val city: String,
    val temp: Int,
    val maxTemp: Int,
    val minTemp: Int,
    val date: Long,
    val weather: String,
    val wind: Float,
    val humidity: Int,
    val pressure: Int
)
