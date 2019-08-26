package com.nstudio.travelreminder.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.os.VibrationEffect
import androidx.core.app.NotificationCompat
import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.ui.MainActivity

class AlarmReceiver : BroadcastReceiver(){

    override fun onReceive(context: Context, intent: Intent) {

        vibrate(context)
        val travelId    = intent.getIntExtra("travelId",0)
        val to          = intent.getStringExtra("to")

        showNotification(context,travelId,"Travel Reminder","You will arrive at $to soon\nDon't forget to take all luggages while \nClick here to check your luggage and journey details")

    }

    private fun vibrate(context: Context){
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //vibrator.vibrate(200)
        }
    }

    private fun showNotification(context: Context,travelId:Int,title:String, message: String) {

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("travelId", travelId)

        val pIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(
            context, "reminder"
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker(message)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pIntent)
            .setAutoCancel(true)

        val nm = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(38, builder.build())

    }

}