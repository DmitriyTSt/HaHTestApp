package ru.dmitriyt.hahtestapp

import android.app.Application
import ru.dmitriyt.hahtestapp.di.AppComponent
import ru.dmitriyt.hahtestapp.di.AppModule
import ru.dmitriyt.hahtestapp.di.DaggerAppComponent

class App : Application() {
    companion object {
        lateinit var INSTANCE: App
            private set
        const val WEATHER_APIKEY = "b0941a2b19d875fbd59a4fc28b0d33f7"
        const val WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }

    val appComponent: AppComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerAppComponent
            .builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        appComponent.inject(this)
    }
}