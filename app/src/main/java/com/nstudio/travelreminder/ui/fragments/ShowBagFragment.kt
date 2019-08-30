package com.nstudio.travelreminder.ui.fragments


import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar

import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.utils.OnActivityInteractionListener
import kotlinx.android.synthetic.main.fragment_show_bag.*
import java.io.File
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

        tvTitle.text = path
        try {
            val root = context!!.getExternalFilesDir("").toString()

            val p = "$root/MyTravels/$path"
            val file = File(p)
            if (file.exists()){
                val bitmap = BitmapFactory.decodeFile(p)
                imgBag.setImageBitmap(bitmap)

                return
            }else{
                Log.e("Image","FNE >> $p")
            }

            Snackbar.make(view,"File not found",Snackbar.LENGTH_SHORT).show()

        }catch (e:Exception){
            e.printStackTrace()
        }

    }


}
