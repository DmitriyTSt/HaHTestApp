package ru.dmitriyt.hahtestapp.domain.repository

import io.reactivex.Single
import ru.dmitriyt.hahtestapp.domain.model.Weather

interface IWeatherRepository {
    fun getWeather(lat: Double, lng: Double): Single<Weather>
}