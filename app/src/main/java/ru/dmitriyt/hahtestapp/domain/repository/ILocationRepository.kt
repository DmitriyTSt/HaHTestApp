package ru.dmitriyt.hahtestapp.domain.repository

import io.reactivex.Single
import ru.dmitriyt.hahtestapp.domain.model.Location

interface ILocationRepository {
    fun getLocation(): Single<Location>
}