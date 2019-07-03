package ru.dmitriyt.hahtestapp.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

class PermissionHelper(private var context: AppCompatActivity) {

    fun checkPermissions(permissions: Array<String>, request: Int): Boolean {
        var result = true
        var showSettings = false
        var showAttention = false
        val requestPermissionsList = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                result = false
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    showAttention = true
                    requestPermissionsList.add(permission)
                } else {
                    if (isFirstRequest(permission)) {
                        requestPermissionsList.add(permission)
                        saveIsFirstRequestFalse(permission)
                    } else {
                        showSettings = true
                    }
                }
            }
        }

        if (!requestPermissionsList.isEmpty()) {
            if (showAttention) {
                showAttention(request, requestPermissionsList.toTypedArray())
            } else {
                ActivityCompat.requestPermissions(context, requestPermissionsList.toTypedArray(), request)
            }
        } else if (showSettings) {
            onApplicationCantAccessPermission(request)
        }

        return result
    }

    private fun showAttention(request: Int, permissions: Array<String>) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Получение прав")
        builder.setMessage("Для работы данной функции пожалуйста предоставьте приложению необходимые разрешения.")
        builder.setPositiveButton("Ок") { dialog, _ ->
            dialog.dismiss()
            ActivityCompat.requestPermissions(context, permissions, request)
        }
        builder.setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun saveIsFirstRequestFalse(permission: String) {
        val sharedPref = context.getSharedPreferences("permissions", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(permission, false)
        editor.apply()
    }

    private fun isFirstRequest(permission: String): Boolean {
        val sharedPref = context.getSharedPreferences("permissions", Context.MODE_PRIVATE)
        return sharedPref.getBoolean(permission, true)
    }

    private fun showPermissionSettingsDialog(request: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Получение прав")
        builder.setMessage("Для работы данной функции пожалуйста предоставьте приложению необходимые разрешения в настройках.")
        builder.setPositiveButton("Настройки") { dialog, _ ->
            dialog.dismiss()
            onClickOkPermissionSettingsDialog(request)
        }
        builder.setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun navigateToApplicationSettings(request: Int) {
        val appSettingsIntent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName))
        context.startActivityForResult(appSettingsIntent, request)
    }

    private fun onClickOkPermissionSettingsDialog(request: Int) {
        navigateToApplicationSettings(request)
    }

    private fun onApplicationCantAccessPermission(request: Int) {
        showPermissionSettingsDialog(request)
    }

}