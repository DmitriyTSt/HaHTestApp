package ru.dmitriyt.hahtestapp.data.model

class WeatherResponse(
    val main: Main
) {
    class Main(
        val temp: Double
    )
}