package com.nstudio.travelreminder.ui.fragments


import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders

import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.ui.viewModels.TravelViewModel
import com.nstudio.travelreminder.utils.OnActivityInteractionListener
import kotlinx.android.synthetic.main.fragment_travel_detials.*
import java.text.SimpleDateFormat
import java.util.*


class TravelDetailsFragment : Fragment() {

    lateinit var viewModel: TravelViewModel
    private lateinit var activityInteractionListener:OnActivityInteractionListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activityInteractionListener = context as OnActivityInteractionListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(activity!!).get(TravelViewModel::class.java)

        return inflater.inflate(R.layout.fragment_travel_detials, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgBackDetails.setOnClickListener {
            activityInteractionListener.onFragmentBack()
        }


        val travelId = arguments!!.getInt("travelId",0)
        Log.e("details", "travel id $travelId")


        val travel = viewModel.getTravel(travelId)

        if (travel!=null){

            tvFromCity.text = travel.from
            tvToCity.text = travel.to

            val formatBase = SimpleDateFormat("dd MMM yy hh:mm a", Locale.ENGLISH)
            val formatTime = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
            val formatMonth = SimpleDateFormat("MMM yy", Locale.ENGLISH)

            var date = formatBase.parse(travel.boardingTime)

            if (date!=null){
                val boardingTime = formatTime.format(date)+
                        "\n"+
                        DateFormat.format("dd", date) as String+" "+
                        DateFormat.format("EEEE", date) as String

                tvFromDay.text = boardingTime
                tvFromMonth.text = formatMonth.format(date)
            }

            date = formatBase.parse(travel.arrivalTime)

            if (date!=null){

                val arrivalTime = formatTime.format(date)+
                        "\n"+
                        DateFormat.format("dd", date) as String+" "+
                        DateFormat.format("EEEE", date) as String

                tvToDay.text = arrivalTime

                tvToMonth.text = formatMonth.format(date)

            }



        }


    }



}
