package com.nstudio.travelreminder.database.entitiy

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "luggage")
data class Luggage(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    @ColumnInfo(name = "travelId") val travelId: Long,
    @ColumnInfo(name = "image") val image:String)