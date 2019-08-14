package com.nstudio.travelreminder.ui.fragments


import android.os.Bundle
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
import com.nstudio.travelreminder.ui.adapters.JourneyListAdapter
import com.nstudio.travelreminder.ui.viewModels.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_travels.*




class TravelListFragment : Fragment() {

    lateinit var viewModel: TravelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(TravelViewModel::class.java)

        return inflater.inflate(R.layout.fragment_travels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.travels.observe(this, Observer {
            list -> run {

                if (list!=null){

                    val journeyListAdapter = JourneyListAdapter(list)
                    rvTravels.adapter = journeyListAdapter

                }
            }
        })

        fabAdd.setOnClickListener {
            view.findNavController().navigate(R.id.createTravelFragment)
        }

    }

}
