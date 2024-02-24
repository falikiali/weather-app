package com.falikiali.weatherapp.di

import com.falikiali.weatherapp.data.ImplWeatherRepository
import com.falikiali.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideWeatherRepository(implWeatherRepository: ImplWeatherRepository): WeatherRepository

}