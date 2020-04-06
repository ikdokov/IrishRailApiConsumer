package com.ikdokov.irishrailconsumer.util

import android.app.Activity
import android.content.Context

object SharedPrefUtils {

    private val SELECTED_STATION = "SELECTED_STATION"

    fun storeSelectedStation(activity: Activity?, station: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(SELECTED_STATION, station)
            commit()
        }
    }

    fun getStoredStation(activity: Activity?): String {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            ?: return ""
        return sharedPref.getString(SELECTED_STATION, "")!!
    }
}