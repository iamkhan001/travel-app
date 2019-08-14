package com.nstudio.travelreminder.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import com.nstudio.travelreminder.database.dao.LuggageDao
import com.nstudio.travelreminder.database.dao.TravelDao
import com.nstudio.travelreminder.database.entitiy.Luggage
import com.nstudio.travelreminder.database.entitiy.Travel
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [(Travel::class), (Luggage::class)], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {


    abstract fun travelDao(): TravelDao
    abstract fun luggageDao(): LuggageDao

    companion object {

        private var sInstance: AppDatabase? = null

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (sInstance == null) {
                sInstance = Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, "travel")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return sInstance!!
        }
    }

}