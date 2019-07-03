package ru.dmitriyt.hahtestapp.presentation.login

import com.arellomobile.mvp.MvpView

interface ILoginView : MvpView {
    fun showErrorLogin(msg: String)
    fun showErrorPassword(msg: String)
    fun loginSuccess()
    fun setLoading(isLoading: Boolean)
    fun showSnackMessage(msg: String)
}