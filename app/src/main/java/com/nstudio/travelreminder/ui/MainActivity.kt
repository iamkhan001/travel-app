package com.nstudio.travelreminder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.utils.OnActivityInteractionListener

class MainActivity : AppCompatActivity(), OnActivityInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState==null){
            val host = NavHostFragment.create(R.navigation.main_path)
            supportFragmentManager.beginTransaction().replace(R.id.fragmentView, host).setPrimaryNavigationFragment(host).commit()

        }
    }


    override fun onFragmentBack() {
        onBackPressed()
    }



}
