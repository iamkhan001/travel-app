package com.nstudio.travelreminder.ui.fragments


import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.utils.OnActivityInteractionListener
import kotlinx.android.synthetic.main.fragment_show_bag.*
import java.lang.Exception


/**
 * A simple [Fragment] subclass.
 *
 */
class ShowBagFragment : Fragment() {

    private lateinit var activityInteractionListener: OnActivityInteractionListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activityInteractionListener = context as OnActivityInteractionListener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_bag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val path = arguments!!.getString("path")

        imgBack.setOnClickListener {
            activityInteractionListener.onFragmentBack()
        }

        try {
            val bitmap = BitmapFactory.decodeFile(path)
            imgBag.setImageBitmap(bitmap)


        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}
