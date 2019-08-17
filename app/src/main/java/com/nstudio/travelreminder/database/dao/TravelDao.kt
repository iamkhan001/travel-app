package com.nstudio.travelreminder.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.nstudio.travelreminder.database.entitiy.Travel
import androidx.room.Update
import androidx.lifecycle.LiveData

@Dao
interface TravelDao{

    @Query("SELECT * FROM travel ORDER BY id DESC")
    fun getAllTravels(): LiveData<List<Travel>>

    @Query("SELECT * FROM travel ORDER BY id DESC")
    fun getAllTravelsVal(): List<Travel>

    @Query("SELECT COUNT(*) from travel")
    fun countTravels(): Int

    @Query("SELECT * FROM travel WHERE id=:id")
    fun getTravelById(id: Int): Travel

    @Query("SELECT * FROM travel WHERE id=:id")
    fun getTravel(id: Int): LiveData<Travel>


    @Insert
    fun insertAll(vararg users: Travel)

    @Insert
    fun insert(travel: Travel): Long

    @Update
    fun update(travel: Travel)

    @Delete
    fun delete(user: Travel)

    @Query("DELETE FROM travel")
    fun deleteAll()

}