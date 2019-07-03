package ru.dmitriyt.hahtestapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.dmitriyt.hahtestapp.data.network.WeatherApiService
import ru.dmitriyt.hahtestapp.data.repository.LocationRepository
import ru.dmitriyt.hahtestapp.data.repository.WeatherRepository
import ru.dmitriyt.hahtestapp.domain.repository.ILocationRepository
import ru.dmitriyt.hahtestapp.domain.repository.IWeatherRepository
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApiService: WeatherApiService): IWeatherRepository =
        WeatherRepository(weatherApiService)

    @Provides
    @Singleton
    fun provideLocationRepository(context: Context): ILocationRepository =
        LocationRepository(context)


}