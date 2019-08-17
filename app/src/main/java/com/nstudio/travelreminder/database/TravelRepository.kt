package com.nstudio.travelreminder.database

import android.app.Application
import com.nstudio.travelreminder.database.dao.TravelDao
import androidx.lifecycle.LiveData
import com.nstudio.travelreminder.database.entitiy.Travel
import android.os.AsyncTask
import android.util.Log
import com.nstudio.travelreminder.ui.viewModels.TravelViewModel
import java.util.concurrent.ExecutionException


class TravelRepository (application: Application, val onDataChangeListener: TravelViewModel.OnDataChangeListener){

    private var travelDao:TravelDao

    init {
        val db = TravelDatabase.getDatabase(application)
        travelDao = db.travelDao()
    }

    fun getmAllTravels(){
        GetTravelList(travelDao,onDataChangeListener).execute()
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getTravel(id: Int): Travel {
        return GetTravel(travelDao).execute(id).get()
    }

    fun insertTravel(travel: Travel) {
        InsertTravelAsync(travelDao,onDataChangeListener).execute(travel)
    }

    fun updateTravel(travel: Travel) {
        UpdateTravelAsync(travelDao,onDataChangeListener).execute(travel)
    }

    fun deleteTravel(travel: Travel) {
        DeleteTravelAsync(travelDao,onDataChangeListener).execute(travel)
    }

    fun deleteAllTravels() {
        DeleteAllTravelsAsync(travelDao,onDataChangeListener).execute()
    }


    companion object{

        private class GetTravel (val travelDao: TravelDao) : AsyncTask<Int,Void, Travel>(){


            override fun doInBackground(vararg ids: Int?): Travel {
                return travelDao.getTravelById(ids[0]!!)
            }

        }

        private class GetTravelList (val travelDao: TravelDao, val onDataChangeListener: TravelViewModel.OnDataChangeListener) : AsyncTask<Int,Void, List<Travel>>(){


            override fun doInBackground(vararg ids: Int?): List<Travel> {
                return travelDao.getAllTravelsVal()
            }

            override fun onPostExecute(result: List<Travel>?) {
                super.onPostExecute(result)
                onDataChangeListener.onTravelListAdd(result)
            }

        }
        
        private class InsertTravelAsync internal constructor(private val travelDao: TravelDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Long>() {

            override fun doInBackground(vararg travel: Travel): Long? {
                val id = travelDao.insert(travel[0])
                Log.e("repo","insert > $id")
                return id
            }

            override fun onPostExecute(result: Long?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,onDataChangeListener).execute()
            }
        }

        private class UpdateTravelAsync internal constructor(private val travelDao: TravelDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.update(travel[0])
                return null
            }
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,onDataChangeListener).execute()
            }
        }

        private class DeleteTravelAsync internal constructor(private val travelDao: TravelDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.delete(travel[0])
                return null
            }
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,onDataChangeListener).execute()
            }
        }

        private class DeleteAllTravelsAsync internal constructor(private val travelDao: TravelDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.deleteAll()
                return null
            }
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,onDataChangeListener).execute()
            }
        }
    }

}