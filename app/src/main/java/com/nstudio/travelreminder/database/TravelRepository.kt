package com.nstudio.travelreminder.database

import com.nstudio.travelreminder.database.dao.TravelDao
import androidx.lifecycle.LiveData
import com.nstudio.travelreminder.database.entitiy.Travel
import android.os.AsyncTask
import java.util.concurrent.ExecutionException


class TravelRepository (val db:AppDatabase,
                        val travelDao: TravelDao,
                        val allTravels: LiveData<List<Travel>>){


    fun getmAllTravels(): LiveData<List<Travel>> {
        return allTravels
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getTravel(id: Int): Travel {
        return GetTravel(travelDao).execute(id).get()
    }

    fun insertTravel(travel: Travel) {
        InsertTravelAsync(travelDao).execute(travel)
    }

    fun updateTravel(travel: Travel) {
        UpdateTravelAsync(travelDao).execute(travel)
    }

    fun deleteTravel(travel: Travel) {
        DeleteTravelAsync(travelDao).execute(travel)
    }

    fun deleteAllTravels() {
        DeleteAllTravelsAsync(travelDao).execute()
    }


    companion object{

        private class GetTravel (val travelDao: TravelDao) : AsyncTask<Int,Void, Travel>(){


            override fun doInBackground(vararg ids: Int?): Travel {
                return travelDao.getTravelById(ids[0]!!)
            }

        }
        
        private class InsertTravelAsync internal constructor(private val travelDao: TravelDao) :
            AsyncTask<Travel, Void, Long>() {

            override fun doInBackground(vararg travel: Travel): Long? {
                return travelDao.insert(travel[0])
            }
        }

        private class UpdateTravelAsync internal constructor(private val travelDao: TravelDao) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.update(travel[0])
                return null
            }
        }

        private class DeleteTravelAsync internal constructor(private val travelDao: TravelDao) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.delete(travel[0])
                return null
            }
        }

        private class DeleteAllTravelsAsync internal constructor(private val travelDao: TravelDao) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.deleteAll()
                return null
            }
        }
    }

}