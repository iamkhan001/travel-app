package com.nstudio.travelreminder.data

import com.nstudio.travelreminder.database.entitiy.Luggage

data class TravelData(val id: Int?,
                      val from:String,
                      val to:String,
                      val boardingTime:String,
                      val arrivalTime:String){

    var images : List<Luggage> = emptyList()
    var bagCount:Int = 0

}