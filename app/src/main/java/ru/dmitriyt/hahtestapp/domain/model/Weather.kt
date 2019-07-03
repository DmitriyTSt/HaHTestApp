package ru.dmitriyt.hahtestapp.domain.model

/**
 * Created by Dmitriy Tomilov on 02/07/2019.
 */

class Weather(
    val temperature: Int
) {
    companion object {
        const val KELVIN_OFFSET = 273
    }
}