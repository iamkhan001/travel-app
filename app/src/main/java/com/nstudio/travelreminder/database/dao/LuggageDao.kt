package com.nstudio.travelreminder.database.dao

import androidx.room.*
import com.nstudio.travelreminder.database.entitiy.Luggage


@Dao
interface LuggageDao{

    @Query("SELECT * FROM luggage WHERE travelId = :travelId")
    fun getAll(travelId:Int): List<Luggage>

    @Query("SELECT COUNT(*) FROM luggage WHERE travelId = :travelId")
    fun countLuggages(travelId: Int): Int

    @Insert
    fun insert(luggage: Luggage):Long

    @Update
    fun update(luggage: Luggage)

    @Delete
    fun delete(luggage: Luggage)


    @Query("DELETE FROM luggage WHERE travelId = :travelId")
    fun deleteAll(travelId:Int)

}