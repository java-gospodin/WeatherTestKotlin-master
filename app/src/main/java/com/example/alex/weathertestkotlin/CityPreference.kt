package com.example.alex.weathertestkotlin

import android.app.Activity
import android.content.SharedPreferences

class CityPreference(activity: MainActivity) {

    var prefs: SharedPreferences? = null

    internal fun getCity(): String? {
        return prefs!!.getString("city", "Krasnodar")
    }

    internal fun setCity(city: String) {
        prefs!!.edit().putString("city", city).apply()
    }

    init {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE)
    }
}