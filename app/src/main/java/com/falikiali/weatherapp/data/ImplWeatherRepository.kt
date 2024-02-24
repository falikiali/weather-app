package com.falikiali.weatherapp.data

import com.falikiali.weatherapp.BuildConfig
import com.falikiali.weatherapp.data.local.WeatherDao
import com.falikiali.weatherapp.data.remote.ApiService
import com.falikiali.weatherapp.data.remote.ErrorResponse
import com.falikiali.weatherapp.domain.model.Weather
import com.falikiali.weatherapp.domain.repository.WeatherRepository
import com.falikiali.weatherapp.helper.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class ImplWeatherRepository @Inject constructor(private val apiService: ApiService, private val weatherDao: WeatherDao): WeatherRepository {
    override suspend fun getCurrentLocationWeather(
        lat: Double,
        lon: Double
    ): Flow<ResultState<String>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val response = apiService.getCurrentLocationWeather(lat, lon, BuildConfig.API_KEY, "metric")
                val latestWeather = weatherDao.getLatestWeather()

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (latestWeather?.date != it.dt || latestWeather.city != it.name) {
                            weatherDao.addCurrentWeather(it.toEntity())
                        }
                        emit(ResultState.Success("Ok"))
                    }
                } else {
                    throw HttpException(response)
                }
            } catch (e: HttpException) {
                e.response()?.errorBody()?.let {
                    val err = Gson().fromJson(it.charStream(), ErrorResponse::class.java)
                    emit(ResultState.Failed(err.message, err.code))
                }
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getWeatherByCity(): Flow<ResultState<List<Weather>>> {
        return flow {
            emit(ResultState.Loading)

            try {
                val response1 = apiService.getWeatherByCity("New York", BuildConfig.API_KEY, "metric")
                val response2 = apiService.getWeatherByCity("Singapore", BuildConfig.API_KEY, "metric")
                val response3 = apiService.getWeatherByCity("Mumbai", BuildConfig.API_KEY, "metric")
                val response4 = apiService.getWeatherByCity("Delhi", BuildConfig.API_KEY, "metric")
                val response5 = apiService.getWeatherByCity("Sydney", BuildConfig.API_KEY, "metric")
                val response6 = apiService.getWeatherByCity("Melbourne", BuildConfig.API_KEY, "metric")

                if (!response1.isSuccessful) {
                    throw HttpException(response1)
                }

                if (!response2.isSuccessful) {
                    throw HttpException(response2)
                }

                if (!response3.isSuccessful) {
                    throw HttpException(response3)
                }

                if (!response4.isSuccessful) {
                    throw HttpException(response4)
                }

                if (!response5.isSuccessful) {
                    throw HttpException(response5)
                }

                if (!response6.isSuccessful) {
                    throw HttpException(response6)
                }

                if (response1.isSuccessful && response2.isSuccessful && response3.isSuccessful && response4.isSuccessful && response5.isSuccessful && response6.isSuccessful) {
                    val listWeather = mutableListOf<Weather>()

                    response1.body()?.let {
                        listWeather.add(it.toDomain())
                    }
                    response2.body()?.let {
                        listWeather.add(it.toDomain())
                    }
                    response3.body()?.let {
                        listWeather.add(it.toDomain())
                    }
                    response4.body()?.let {
                        listWeather.add(it.toDomain())
                    }
                    response5.body()?.let {
                        listWeather.add(it.toDomain())
                    }
                    response6.body()?.let {
                        listWeather.add(it.toDomain())
                    }

                    emit(ResultState.Success(listWeather))
                }

            } catch (e: HttpException) {
                e.response()?.errorBody()?.let {
                    val err = Gson().fromJson(it.charStream(), ErrorResponse::class.java)
                    emit(ResultState.Failed(err.message, err.code))
                }
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString(), 0))
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getCurrentLocationFromDb(): Flow<List<Weather>> {
        return weatherDao.getCurrentLocationWeatherFromDb().map {
            it.map { weatherEntity ->
                weatherEntity.toDomain()
            }
        }
    }
}