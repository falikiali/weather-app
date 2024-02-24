package com.falikiali.weatherapp.data.remote

import com.falikiali.weatherapp.data.local.WeatherEntity
import com.falikiali.weatherapp.domain.model.Weather
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
	@field:SerializedName("main") val main: MainResponse,
	@field:SerializedName("sys") val sys: SysResponse,
	@field:SerializedName("dt") val dt: Long,
	@field:SerializedName("weather") val weather: List<WeatherItemResponse>,
	@field:SerializedName("name") val name: String,
	@field:SerializedName("cod") val cod: Int,
	@field:SerializedName("id") val id: Int,
	@field:SerializedName("wind") val wind: WindResponse
) {
	fun toEntity(): WeatherEntity {
		return WeatherEntity(
			city = name,
			temp = main.temp.toInt(),
			maxTemp = main.tempMax.toInt(),
			minTemp = main.tempMin.toInt(),
			date = dt,
			weather = weather[0].main,
			wind = wind.speed,
			humidity = main.humidity,
			pressure = main.pressure
		)
	}

	fun toDomain(): Weather {
		return Weather(
			id,
			name,
			main.temp.toInt(),
			main.tempMax.toInt(),
			main.tempMin.toInt(),
			dt,
			weather[0].main,
			wind.speed,
			main.humidity,
			main.pressure
		)
	}
}

data class MainResponse(
	@field:SerializedName("temp") val temp: Float,
	@field:SerializedName("temp_min") val tempMin: Float,
	@field:SerializedName("humidity") val humidity: Int,
	@field:SerializedName("pressure") val pressure: Int,
	@field:SerializedName("temp_max") val tempMax: Float
)

data class SysResponse(
	@field:SerializedName("country") val country: String,
	@field:SerializedName("id") val id: Int,
	@field:SerializedName("type") val type: Int
)

data class WeatherItemResponse(
	@field:SerializedName("main") val main: String,
	@field:SerializedName("id") val id: Int
)

data class WindResponse(
	@field:SerializedName("speed") val speed: Float,
)
