package com.nstudio.travelreminder.utils

import android.content.Context
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent


class AlarmUtils (private val context:Context){

    companion object{
        private const val ALARM_REQ = 1
    }

    fun setAlarm(timestamp:Long){
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQ, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager!!.set(AlarmManager.RTC_WAKEUP, timestamp, pendingIntent)
    }

}