package ru.dmitriyt.hahtestapp.data.repository

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import ru.dmitriyt.hahtestapp.domain.model.Location
import ru.dmitriyt.hahtestapp.domain.repository.ILocationRepository

class LocationRepository(private val context: Context) : ILocationRepository {
    @SuppressLint("MissingPermission")
    override fun getLocation(): Single<Location> {
        return Single.create { singleEmitter ->
            LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.let { loc ->
                        singleEmitter.onSuccess(Location(loc.latitude, loc.longitude))
                    }
                }
            }
        }
    }
}