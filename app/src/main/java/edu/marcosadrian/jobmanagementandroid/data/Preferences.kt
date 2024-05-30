package edu.marcosadrian.jobmanagementandroid.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class Preferences(context: Context) {
    val PREFS_NAME = "es.marcosadrian.mypreferences"
    val SHARED_USER = "shared_name"
    val SHARED_PASSWORD = "shared_password"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    // Se crea la propiedad name que será persistente, además se modifica
    // su getter y setter para que almacene en SharedPreferences.
    var idTrabajador: String
        get() = prefs.getString(SHARED_USER, "").toString()
        set(value) = prefs.edit().putString(SHARED_USER, value).apply()

    var password: String
        get() = prefs.getString(SHARED_PASSWORD, "").toString()
        set(value) = prefs.edit().putString(SHARED_PASSWORD, value).apply()

    // Se eliminan las preferencias.
    fun deletePrefs() {
        prefs.edit().apply {
            remove(SHARED_USER)
            remove(SHARED_PASSWORD)
            apply()
        }
    }
}