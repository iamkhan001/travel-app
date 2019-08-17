package com.nstudio.travelreminder.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.ui.viewModels.TravelViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nstudio.travelreminder.database.entitiy.Travel
import com.nstudio.travelreminder.ui.adapters.JourneyListAdapter
import com.nstudio.travelreminder.ui.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_travels.*




class TravelListFragment : Fragment() {

    lateinit var viewModel: TravelViewModel
    private val TAG = TravelListFragment::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(activity!!).get(TravelViewModel::class.java)

        return inflater.inflate(R.layout.fragment_travels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clickListener = JourneyListAdapter.OnTravelClickListener(function = {
            travel ->
            run {
                val data = Bundle()
                data.putInt("travelId", travel.id!!)
                Log.e(TAG,"travelId ${travel.id}")
                view.findNavController().navigate(R.id.travelDetailsFragment,data)
            }
        })

        viewModel.travels.observe(this, Observer {
            list -> run {


            Log.e(TAG,"list called ")

                if (list!=null){
                    Log.e(TAG,"list > "+list.size)
                    val journeyListAdapter = JourneyListAdapter(list,clickListener)
                    rvTravels.layoutManager = LinearLayoutManager(context)
                    rvTravels.adapter = journeyListAdapter

                }
            }
        })

        fabAdd.setOnClickListener {
            view.findNavController().navigate(R.id.createTravelFragment)
        }

    }


}
