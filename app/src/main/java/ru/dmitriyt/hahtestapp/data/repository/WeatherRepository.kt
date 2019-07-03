package ru.dmitriyt.hahtestapp.data.repository

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.dmitriyt.hahtestapp.App
import ru.dmitriyt.hahtestapp.data.network.WeatherApiService
import ru.dmitriyt.hahtestapp.data.model.WeatherResponse
import ru.dmitriyt.hahtestapp.domain.model.Weather
import ru.dmitriyt.hahtestapp.domain.repository.IWeatherRepository

class WeatherRepository(private val weatherApiService: WeatherApiService) : IWeatherRepository {
    override fun getWeather(lat: Double, lng: Double): Single<Weather> {
        return weatherApiService.getWeather(lat, lng, App.WEATHER_APIKEY)
            .map {
                Weather(it.main.temp.toInt() - Weather.KELVIN_OFFSET)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}