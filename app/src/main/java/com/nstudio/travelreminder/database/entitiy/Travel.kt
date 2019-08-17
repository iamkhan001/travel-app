package com.nstudio.travelreminder.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "travel")
data class Travel(@PrimaryKey(autoGenerate = true) val id: Int?,
                  @ColumnInfo(name = "from") val from:String,
                  @ColumnInfo(name = "to") val to:String,
                  @ColumnInfo(name = "boarding_time") val boardingTime:String,
                  @ColumnInfo(name = "arrival_time") val arrivalTime:String)