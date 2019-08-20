package com.nstudio.travelreminder.utils

import android.content.Context
import android.content.SharedPreferences

class AppDataProvider (val context:Context){

    private val sharedPreferences:SharedPreferences = context.getSharedPreferences("app_data",Context.MODE_PRIVATE)

    fun isPermissionAsked():Boolean{
        return sharedPreferences.getBoolean("isPermissionAsked",false)
    }

    fun dontAskPermission(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("isPermissionAsked",true)
        editor.apply()
    }

}