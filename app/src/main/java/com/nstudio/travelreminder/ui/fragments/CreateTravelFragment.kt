package com.nstudio.travelreminder.ui.fragments


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.ui.dialogs.ImageChooserDialog
import com.nstudio.travelreminder.utils.OnActivityInteractionListener
import kotlinx.android.synthetic.main.fragment_create_travel.*
import java.text.SimpleDateFormat
import java.util.*
import android.app.DatePickerDialog
import android.util.Log
import java.text.ParseException
import android.app.TimePickerDialog
import com.google.android.material.snackbar.Snackbar


class CreateTravelFragment : Fragment() {

    lateinit var activityInteractionListener: OnActivityInteractionListener

    private lateinit var  timeFormat: SimpleDateFormat
    private lateinit var  dateFormat: SimpleDateFormat
    private lateinit var  baseDateFormat: SimpleDateFormat
    private lateinit var  baseTimeFormat: SimpleDateFormat

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0

    private var boardingDate = ""
    private var boardingTime = ""
    private var arrivalDate = ""
    private var arrivalTime = ""

    private val methodSelectListener = ImageChooserDialog.OnMethodSelectListener { m ->
        if(m==0){

        }else{

        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        activityInteractionListener = context as OnActivityInteractionListener

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //dateFormat = SimpleDateFormat("EE dd MMM yy", Locale.ENGLISH)

        dateFormat = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)
        timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        baseDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        baseTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)


        return inflater.inflate(R.layout.fragment_create_travel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()


    }

    private fun init(){
        imgBack.setOnClickListener {
            activityInteractionListener.onFragmentBack()
        }

        tvAddImage.setOnClickListener {
            val imageChooserDialog = ImageChooserDialog(context!!,methodSelectListener)
            imageChooserDialog.show()
        }

        tvFromDate.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(context!!,
                { view, year, monthOfYear, dayOfMonth -> setFromDate(dayOfMonth, monthOfYear + 1, year) },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()

        }

        tvToDate.setOnClickListener {
            val c = Calendar.getInstance()
            mYear = c.get(Calendar.YEAR)
            mMonth = c.get(Calendar.MONTH)
            mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(context!!,
                { view, year, monthOfYear, dayOfMonth -> setToDate(dayOfMonth, monthOfYear + 1, year) },
                mYear,
                mMonth,
                mDay
            )
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        tvFromTime.setOnClickListener {
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR)
            mMinute = c.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(context,
                { view, hourOfDay, minute -> setFromTime(hourOfDay, minute) }, mHour, mMinute, false
            )
            timePickerDialog.show()

        }
        tvToTime.setOnClickListener {
            val c = Calendar.getInstance()
            mHour = c.get(Calendar.HOUR)
            mMinute = c.get(Calendar.MINUTE)
            val timePickerDialog = TimePickerDialog(context,
                { view, hourOfDay, minute -> setToTime(hourOfDay, minute) }, mHour, mMinute, false
            )
            timePickerDialog.show()
        }


        val dt = Date()
        val c = Calendar.getInstance()
        c.time = dt
        c.add(Calendar.DATE, 1)
        setFromDate(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))
        setToDate(c.get(Calendar.DAY_OF_MONTH)+1, c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR))

        setFromTime(9, 0)
        setToTime(9, 0)


        btnSave.setOnClickListener { v->
            run {

                val from = etFrom.text.toString().trim()
                if (from.length<3){
                    mfFrom.error = "Enter Valid Boarding City"
                    return@setOnClickListener
                }

                val to = etTo.text.toString().trim()
                if(to.length<3){
                    mfTo.error = "Enter Valid Destination City"
                    return@setOnClickListener
                }

                if (boardingDate.isEmpty()){
                    Snackbar.make(v,"Select Boarding Date",Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                if (boardingTime.isEmpty()){
                    Snackbar.make(v,"Select Boarding Time",Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                if (arrivalDate.isEmpty()){
                    Snackbar.make(v,"Select Arrival Date",Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }


                if (arrivalTime.isEmpty()){
                    Snackbar.make(v,"Select Arrival Time",Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }




            }
        }
    }

    private fun setFromDate(day: Int, month: Int, year: Int) {

        var date: Date? = null
        try {
            date = baseDateFormat.parse("$year-$month-$day")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (date == null) {
            Log.e(tag, "date is null")
            return
        }

        boardingDate =  dateFormat.format(date)

        Log.e(tag, "from date > $boardingDate")

        tvFromDate.text = boardingDate

    }

    private fun setToDate(day: Int, month: Int, year: Int) {

        var date: Date? = null
        try {
            date = baseDateFormat.parse("$year-$month-$day")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (date == null) {
            Log.e(tag, "date is null")
            return
        }

        arrivalDate =  dateFormat.format(date)

        Log.e(tag, "to date > $arrivalDate")

        tvToDate.text = arrivalDate

    }

    private fun setFromTime(hourOfDay: Int, minute: Int) {

        var time: Date? = null
        try {
            time = baseTimeFormat.parse("$hourOfDay:$minute:00")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (time == null) {
            Log.e(tag, "time is null")
            return
        }

        boardingTime = timeFormat.format(time)

        tvFromTime.text = boardingTime

        Log.e(tag, "from time $boardingTime")
    }

    private fun setToTime(hourOfDay: Int, minute: Int) {

        var time: Date? = null
        try {
            time = baseTimeFormat.parse("$hourOfDay:$minute:00")
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (time == null) {
            Log.e(tag, "time is null")
            return
        }

        arrivalTime = timeFormat.format(time)

        tvToTime.text = arrivalTime

        Log.e(tag, "to time $arrivalTime")
    }

}
