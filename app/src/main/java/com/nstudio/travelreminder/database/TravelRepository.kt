package com.nstudio.travelreminder.database

import android.app.Application
import com.nstudio.travelreminder.database.dao.TravelDao
import com.nstudio.travelreminder.database.entitiy.Travel
import android.os.AsyncTask
import android.util.Log
import com.nstudio.travelreminder.data.TravelData
import com.nstudio.travelreminder.database.dao.LuggageDao
import com.nstudio.travelreminder.database.entitiy.Luggage
import com.nstudio.travelreminder.ui.viewModels.TravelViewModel
import java.util.ArrayList
import java.util.concurrent.ExecutionException


class TravelRepository (application: Application, private val onDataChangeListener: TravelViewModel.OnDataChangeListener){

    private var travelDao:TravelDao
    private var luggageDao:LuggageDao

    init {
        val db = TravelDatabase.getDatabase(application)
        travelDao = db.travelDao()
        luggageDao = db.luggageDao()
    }

    fun getmAllTravels(){
        GetTravelList(travelDao,luggageDao,onDataChangeListener).execute()
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getTravel(id: Int): TravelData {
        return GetTravel(travelDao,luggageDao).execute(id).get()
    }

    fun insertTravel(
        travel: Travel,
        luggageImages: ArrayList<String>
    ) {
        InsertTravelAsync(travelDao,luggageDao,luggageImages,onDataChangeListener).execute(travel)
    }

    fun updateTravel(travel: Travel) {
        UpdateTravelAsync(travelDao,luggageDao,onDataChangeListener).execute(travel)
    }

    fun deleteTravel(travel: Travel) {
        DeleteTravelAsync(travelDao,luggageDao,onDataChangeListener).execute(travel)
    }

    fun deleteAllTravels() {
        DeleteAllTravelsAsync(travelDao,luggageDao,onDataChangeListener).execute()
    }

    fun removeLuggage(luggage: Luggage) {
        DeleteLuggageAsync(luggageDao).execute(luggage)
    }


    companion object{

        private class GetTravel (val travelDao: TravelDao, val luggageDao: LuggageDao) : AsyncTask<Int,Void, TravelData>(){


            override fun doInBackground(vararg ids: Int?): TravelData {
                val travel = travelDao.getTravelById(ids[0]!!)
                val travelData = TravelData(travel.id,travel.from,travel.to,travel.boardingTime,travel.arrivalTime)

                travelData.images = luggageDao.getAll(travel.id!!)
                return travelData
            }

        }

        private class GetTravelList (val travelDao: TravelDao,val luggageDao: LuggageDao, val onDataChangeListener: TravelViewModel.OnDataChangeListener) : AsyncTask<Int,Void, List<TravelData>>(){


            override fun doInBackground(vararg ids: Int?): List<TravelData> {
                val travels = travelDao.getAllTravelsVal()

                val list = ArrayList<TravelData>()

                for (travel in travels){
                    val data = TravelData(travel.id,travel.from,travel.to,travel.boardingTime,travel.arrivalTime)
                    data.bagCount = luggageDao.countLuggages(travel.id!!)
                    list.add(data)
                }

                return list

            }

            override fun onPostExecute(result: List<TravelData>?) {
                super.onPostExecute(result)
                onDataChangeListener.onTravelListAdd(result)
            }

        }
        
        private class InsertTravelAsync internal constructor(
            private val travelDao: TravelDao,
            private val luggageDao: LuggageDao,
            private val images: ArrayList<String>,
            private val onDataChangeListener: TravelViewModel.OnDataChangeListener
        ) :
            AsyncTask<Travel, Void, Long>() {

            override fun doInBackground(vararg travel: Travel): Long? {
                val id = travelDao.insert(travel[0])

                Log.e("repo","total luggage > ${images.size} ")
                Log.e("repo","travel insert > $id ")
                for (i in images){
                    luggageDao.insert(Luggage(null,id,i))
                    Log.d("repo","luggage insert > $id")
                }

                return id
            }

            override fun onPostExecute(result: Long?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,luggageDao,onDataChangeListener).execute()
            }
        }

        private class UpdateTravelAsync internal constructor(private val travelDao: TravelDao,private val luggageDao: LuggageDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.update(travel[0])
                return null
            }
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,luggageDao,onDataChangeListener).execute()
            }
        }

        private class DeleteTravelAsync internal constructor(private val travelDao: TravelDao,private val luggageDao: LuggageDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.delete(travel[0])
                return null
            }
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,luggageDao,onDataChangeListener).execute()
            }
        }

        private class DeleteLuggageAsync internal constructor(private val luggageDao: LuggageDao) :
            AsyncTask<Luggage, Void, Void>() {

            override fun doInBackground(vararg luggage: Luggage?): Void? {
                luggageDao.delete(luggage[0]!!)
                return null
            }


        }

        private class DeleteAllTravelsAsync internal constructor(private val travelDao: TravelDao,private val luggageDao: LuggageDao,private val onDataChangeListener: TravelViewModel.OnDataChangeListener) :
            AsyncTask<Travel, Void, Void>() {

            override fun doInBackground(vararg travel: Travel): Void? {
                travelDao.deleteAll()
                return null
            }
            override fun onPostExecute(result: Void?) {
                super.onPostExecute(result)
                GetTravelList(travelDao,luggageDao,onDataChangeListener).execute()
            }
        }
    }

}