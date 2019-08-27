package com.nstudio.travelreminder.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer

import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.ui.viewModels.TravelViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nstudio.travelreminder.ui.adapters.JourneyListAdapter
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

                    val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom)
                    rvTravels.layoutAnimation = controller
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
