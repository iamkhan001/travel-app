package com.nstudio.travelreminder.database

import androidx.lifecycle.LiveData
import android.os.AsyncTask
import com.nstudio.travelreminder.database.dao.LuggageDao
import com.nstudio.travelreminder.database.entitiy.Luggage
import java.util.concurrent.ExecutionException


class LuggageRepository (private val db:TravelDatabase,
                         private val luggageDao: LuggageDao,
                         private val luggages: LiveData<List<Luggage>>){


    fun getAllLuggages(): LiveData<List<Luggage>> {
        return luggages
    }

    fun getLuggageCount(travelId: Int) : Int{
        return GetLuggageCountAsync(luggageDao).execute(travelId).get()
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    fun getLuggage(id: Int): List<Luggage> {
        return GetAllLuggageAsync(luggageDao).execute(id).get()
    }

    fun insertTravel(luggage: Luggage) {
        InsertLuggageAsync(luggageDao).execute(luggage)
    }

    fun updateTravel(luggage: Luggage) {
        UpdateLuggageAsync(luggageDao).execute(luggage)
    }

    fun deleteTravel(luggage: Luggage) {
        DeleteLuggageAsync(luggageDao).execute(luggage)
    }

    fun deleteAllLuggages(travelId:Int) {
        DeleteAllLuggageAsync(luggageDao).execute(travelId)
    }


    companion object{

        private class GetAllLuggageAsync (val luggageDao: LuggageDao) : AsyncTask<Int,Void,List<Luggage>>(){

            override fun doInBackground(vararg ids: Int?): List<Luggage> {
                return luggageDao.getAll(ids[0]!!)
            }
        }

        private class GetLuggageCountAsync (val luggageDao: LuggageDao) : AsyncTask<Int, Void, Int>(){
            override fun doInBackground(vararg ids: Int?): Int {
                return luggageDao.countLuggages(ids[0]!!)
            }

        }
        
        private class InsertLuggageAsync internal constructor(private val luggageDao: LuggageDao) :
            AsyncTask<Luggage, Void, Long>() {
            override fun doInBackground(vararg luggage: Luggage): Long {
                return  luggageDao.insert(luggage[0])
            }
        }

        private class UpdateLuggageAsync internal constructor(private val luggageDao: LuggageDao) :
            AsyncTask<Luggage, Void, Void>() {
            override fun doInBackground(vararg luggage: Luggage): Void? {
                luggageDao.update(luggage[0])
                return null
            }
        }

        private class DeleteLuggageAsync internal constructor(private val luggageDao: LuggageDao) :
            AsyncTask<Luggage, Void, Void>() {

            override fun doInBackground(vararg luggage: Luggage): Void? {
                luggageDao.delete(luggage[0])
                return null
            }
        }

        private class DeleteAllLuggageAsync internal constructor(private val luggageDao: LuggageDao) :
            AsyncTask<Int, Void, Void>() {

            override fun doInBackground(vararg ids: Int?): Void? {
                luggageDao.deleteAll(ids[0]!!)
                return null
            }

        }
    }

}