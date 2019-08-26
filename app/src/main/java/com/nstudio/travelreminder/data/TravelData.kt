package com.nstudio.travelreminder.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nstudio.travelreminder.database.entitiy.Luggage

@Entity(tableName = "travel")
data class TravelData(@PrimaryKey(autoGenerate = true) val id: Int?,
                      @ColumnInfo(name = "from") val from:String,
                      @ColumnInfo(name = "to") val to:String,
                      @ColumnInfo(name = "boarding_time") val boardingTime:String,
                      @ColumnInfo(name = "arrival_time") val arrivalTime:String){

    var images : List<Luggage> = emptyList()

    constructor(
        id: Int?,
        from:String,
        to:String,
        boardingTime:String,
        arrivalTime:String,
        images : List<Luggage>
    ) : this(id,from,to,boardingTime,arrivalTime){
        this.images = images
    }

}