package ru.dmitriyt.hahtestapp.presentation.login

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.dmitriyt.hahtestapp.R
import ru.dmitriyt.hahtestapp.domain.repository.ILocationRepository
import ru.dmitriyt.hahtestapp.domain.repository.IWeatherRepository
import ru.dmitriyt.hahtestapp.ui.login.LoginActivity
import javax.inject.Inject

@InjectViewState
class LoginPresenter @Inject constructor(
    private val weatherRepository: IWeatherRepository,
    private val locationRepository: ILocationRepository,
    private val appContext: Context
) : MvpPresenter<ILoginView>() {

    private val compositeDisposable = CompositeDisposable()

    fun checkPlayServices(activity: AppCompatActivity) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, LoginActivity.PLAY_SERVICES_RESOLUTION_REQUEST)
            }
            viewState.showSnackMessage("Сервисы google play недоступны")
        }
    }

    fun login(login: String, password: String) {
        if (!login.contains(Regex("^[a-zA-Z0-9._]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$"))) {
            viewState.showErrorLogin(appContext.resources.getString(R.string.email_error))
            return
        }
        if (password.length < 6) {
            viewState.showErrorPassword(appContext.resources.getString(R.string.password_length_error))
            return
        }
        if (!password.contains(Regex("[a-z]+")) ||
            !password.contains(Regex("[A-Z]+")) || !password.contains(Regex("\\d+"))
        ) {
            viewState.showErrorPassword(appContext.resources.getString(R.string.password_content_error))
            return
        }
        viewState.loginSuccess()
    }

    fun getPosition() {
        viewState.setLoading(true)
        locationRepository.getLocation().subscribe({
            getWeather(it.lat, it.lng)
        }, {
            viewState.apply {
                setLoading(false)
                showSnackMessage(it.message ?: "")
            }
        }).disposeOnPresenterDestroy()
    }

    private fun getWeather(lat: Double, lng: Double) {
        weatherRepository.getWeather(lat, lng).subscribe({
            viewState.apply {
                setLoading(false)
                showSnackMessage(appContext.resources.getString(R.string.weather_message, it.temperature))
            }
        }, {
            viewState.apply {
                setLoading(false)
                showSnackMessage(it.message ?: "")
            }
        }).disposeOnPresenterDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    fun Disposable.disposeOnPresenterDestroy() {
        compositeDisposable.add(this)
    }
}