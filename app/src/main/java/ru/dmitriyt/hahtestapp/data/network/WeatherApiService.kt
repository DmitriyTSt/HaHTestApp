package ru.dmitriyt.hahtestapp.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.dmitriyt.hahtestapp.App
import ru.dmitriyt.hahtestapp.data.model.WeatherResponse

interface WeatherApiService {
    @GET("weather ")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") apikey: String
    ): Single<WeatherResponse>
}