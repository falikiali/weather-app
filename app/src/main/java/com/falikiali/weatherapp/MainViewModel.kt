package com.falikiali.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.falikiali.weatherapp.domain.model.Weather
import com.falikiali.weatherapp.domain.usecase.WeatherUseCase
import com.falikiali.weatherapp.helper.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val weatherUseCase: WeatherUseCase): ViewModel() {

    init {
        getCurrentLocationWeatherFromDB()
    }

    private val _result = MutableLiveData<ResultState<String>>()
    val result: LiveData<ResultState<String>> get() = _result

    private val _resultDb = MutableLiveData<List<Weather>>()
    val resultDb: LiveData<List<Weather>> get() = _resultDb

    private val _resultByCity = MutableLiveData<ResultState<List<Weather>>>()
    val resultByCity: LiveData<ResultState<List<Weather>>> get() = _resultByCity

    fun getCurrentLocationWeather(lat: Double, lon: Double) {
        viewModelScope.launch {
            weatherUseCase.getCurrentLocationWeather(lat, lon).collect {
                _result.postValue(it)
            }
        }
    }

    fun getCurrentLocationWeatherFromDB() {
        viewModelScope.launch {
            weatherUseCase.getCurrentLocationFromDb().collect {
                _resultDb.postValue(it)
            }
        }
    }

    fun getCurrentWeatherByCity() {
        viewModelScope.launch {
            weatherUseCase.getWeatherByCity().collect {
                _resultByCity.postValue(it)
            }
        }
    }

}