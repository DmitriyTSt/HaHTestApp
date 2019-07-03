package ru.dmitriyt.hahtestapp.di

import dagger.Component
import ru.dmitriyt.hahtestapp.App
import ru.dmitriyt.hahtestapp.ui.login.LoginActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, RepositoryModule::class])

interface AppComponent {
    fun inject(application: App)

    fun inject(activity: LoginActivity)
}