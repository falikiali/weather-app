package com.falikiali.weatherapp.di

import com.falikiali.weatherapp.domain.usecase.ImplWeatherUseCase
import com.falikiali.weatherapp.domain.usecase.WeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped
    abstract fun provideWeatherUseCase(implWeatherUseCase: ImplWeatherUseCase): WeatherUseCase

}