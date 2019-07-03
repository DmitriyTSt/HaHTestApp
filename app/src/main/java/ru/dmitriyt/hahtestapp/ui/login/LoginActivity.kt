package ru.dmitriyt.hahtestapp.ui.login

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_login.*
import ru.dmitriyt.hahtestapp.App
import ru.dmitriyt.hahtestapp.R
import ru.dmitriyt.hahtestapp.data.system.hideErrorOnTextChange
import ru.dmitriyt.hahtestapp.presentation.login.ILoginView
import ru.dmitriyt.hahtestapp.presentation.login.LoginPresenter
import ru.dmitriyt.hahtestapp.utils.PermissionHelper
import javax.inject.Inject

class LoginActivity : MvpAppCompatActivity(), ILoginView, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

    companion object {
        const val CHECK_PERMISSION_REQUEST = 1
        const val PLAY_SERVICES_RESOLUTION_REQUEST = 2
    }

    init {
        App.INSTANCE.appComponent.inject(this@LoginActivity)
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: LoginPresenter

    @ProvidePresenter
    fun providePresenter() = presenter

    private var googleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        login_edit.hideErrorOnTextChange(login_layout)
        password_edit.hideErrorOnTextChange(password_layout)
        btn_login.setOnClickListener {
            presenter.login(login_edit.text.toString(), password_edit.text.toString())
        }
        password_edit.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                btn_login.callOnClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        btn_create.setOnClickListener {
            Toast.makeText(this, R.string.btn_create_account_message, Toast.LENGTH_SHORT).show()
        }
        btn_restore.setOnClickListener {
            Toast.makeText(this, R.string.btn_restore_password_message, Toast.LENGTH_SHORT).show()
        }
        presenter.checkPlayServices(this)
    }

    override fun showSnackMessage(msg: String) {
        Snackbar.make(main_wrap, msg, Snackbar.LENGTH_LONG).show()
    }

    private var progressDialog: AlertDialog? = null

    override fun setLoading(isLoading: Boolean) {
        progressDialog?.dismiss()
        if (isLoading) {
            progressDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_progress)
                .setCancelable(false)
                .create()
            progressDialog?.show()
        }
    }

    override fun loginSuccess() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        checkPermission()
    }

    override fun onConnected(p0: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        presenter.getPosition()

    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onStop() {
        super.onStop()
        setLoading(false)
    }

    override fun showErrorLogin(msg: String) {
        login_layout.error = msg
    }

    override fun showErrorPassword(msg: String) {
        password_layout.error = msg
    }

    private fun checkPermission() {
        val permissionHelper = PermissionHelper(this)
        if (permissionHelper.checkPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                CHECK_PERMISSION_REQUEST
            )
        ) {
            getPosition()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CHECK_PERMISSION_REQUEST) {
            checkPermission()
        }
    }

    private fun getPosition() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        googleApiClient?.connect()
    }
}