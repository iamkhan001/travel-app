package com.nstudio.travelreminder.ui.fragments


import android.Manifest
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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.nstudio.travelreminder.database.entitiy.Travel
import com.nstudio.travelreminder.ui.viewModels.TravelViewModel
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.content.Intent
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.nstudio.travelreminder.database.model.Image
import com.nstudio.travelreminder.ui.adapters.ImageListAdapter
import com.nstudio.travelreminder.utils.AlarmUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.collections.ArrayList


class CreateTravelFragment : Fragment() {

    private lateinit var activityInteractionListener: OnActivityInteractionListener

    companion object {

        private const val PERMISSION_CALLBACK_CONSTANT = 100
        private const val REQUEST_PERMISSION_SETTING = 101
        private const val READ_REQUEST_CODE = 103
        private const val REQUEST_IMAGE_CAPTURE = 104

    }


    private lateinit var  timeFormat: SimpleDateFormat
    private lateinit var  saveFormat: SimpleDateFormat
    private lateinit var  dateFormat: SimpleDateFormat
    private lateinit var  decodeFormat: SimpleDateFormat
    private lateinit var  baseDateFormat: SimpleDateFormat
    private lateinit var  baseTimeFormat: SimpleDateFormat

    private val TAG = CreateTravelFragment::class.java.simpleName

    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var mHour: Int = 0
    private var mMinute: Int = 0

    private var boardingDate = ""
    private var boardingTime = ""
    private var arrivalDate = ""
    private var arrivalTime = ""
    private lateinit var viewModel: TravelViewModel
    private var isPhotoCaptured = false
    private var photoFile:File? = null
    private var photoURI: Uri? = null
    private var mCurrentPhotoPath = ""
    private var imageList = ArrayList<Image>()
    private var imageListAdapter = ImageListAdapter(imageList)

    private val methodSelectListener = ImageChooserDialog.OnMethodSelectListener { m ->
        if(m==0){
            //camera
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(context!!.packageManager) != null) {
                photoFile = null
                try {
                    photoFile = createImageFile()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    return@OnMethodSelectListener
                }

                if (photoFile != null) {
                    photoURI = FileProvider.getUriForFile(
                        context!!,
                        "com.nstudio.android.fileprovider",
                        photoFile!!
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }

        }else{
            //file
            openFilePicker();
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

        viewModel = ViewModelProviders.of(activity!!).get(TravelViewModel::class.java)

        dateFormat = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)
        decodeFormat = SimpleDateFormat("dd MMM yy hh:mm a", Locale.ENGLISH)
        timeFormat = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        baseDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        baseTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
        saveFormat = SimpleDateFormat("dd-MMM-yy-HH:mm:ss", Locale.ENGLISH)


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
            if (checkPermissions()){
                val imageChooserDialog = ImageChooserDialog(context!!,methodSelectListener)
                imageChooserDialog.show()
            }
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

                val travel = Travel(
                    null,
                    from,
                    to,
                    "$boardingDate $boardingTime",
                    "$arrivalDate $arrivalTime"
                )

                val dateArrival = "$arrivalDate $arrivalTime"
                val dateBoarding = "$boardingDate $boardingTime"

                progressBar.visibility = View.VISIBLE

                val id = viewModel.addTravel(travel)

                try {

                    val dateA = decodeFormat.parse(dateArrival)
                    val dateB = decodeFormat.parse(dateBoarding)

                    val alarm = AlarmUtils(context!!)

                    if (dateA!=null){
                        alarm.setAlarm(dateA.time)
                    }else{
                        Log.e(TAG,"Arrival Date is null")
                    }

                    if (dateB!=null){
                        alarm.setAlarm(dateB.time)
                    }else{
                        Log.e(TAG,"Boarding Date is null")
                    }

                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }


                for (image: Image in imageList){
                    saveImage(image)
                }

                progressBar.visibility = View.GONE

                Snackbar.make(v,"Journey Added Successfully",Snackbar.LENGTH_SHORT).show()
                activity!!.supportFragmentManager.popBackStackImmediate()

            }
        }

        rvLuggage.layoutManager = GridLayoutManager(context,2)
        rvLuggage.adapter = imageListAdapter
    }

    private fun saveImage(image: Image) {

        val root = context!!.getExternalFilesDir("").toString()
        val myDir = File("$root/MyTravels")
        myDir.mkdirs()
        val n = saveFormat.format(Date())
        val fName = "luggage--$n.jpg"
        val file = File(myDir, fName)
        Log.i(TAG, "" + file)
        if (file.exists())
            file.delete()
        try {
            val out = FileOutputStream(file)
            image.bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            image.name = fName
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
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

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.absolutePath
        return image
    }


    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor = context!!.contentResolver.openFileDescriptor(uri, "r")!!
        val fileDescriptor = parcelFileDescriptor.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    private fun galleryAddPic() {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(mCurrentPhotoPath)
        val contentUri = Uri.fromFile(f)
        mediaScanIntent.data = contentUri
        context!!.sendBroadcast(mediaScanIntent)
    }


    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, READ_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

      if (requestCode == READ_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                photoURI = data.data
                if (photoURI != null) {
                    Log.e("uri", "Uri: " + photoURI.toString())
                    try {

                        val bitmap = getBitmapFromUri(photoURI!!)
                        imageList.add(Image(bitmap,""))
                        imageListAdapter.notifyItemInserted(imageList.size)
                        isPhotoCaptured = false
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            galleryAddPic()
            var bitmap:Bitmap
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, photoURI)
                bitmap = cropAndScale(bitmap, 300)
                imageList.add(Image(bitmap,""))
                imageListAdapter.notifyItemInserted(imageList.size)
                isPhotoCaptured = true
            } catch (e:Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun cropAndScale(source: Bitmap, scale: Int): Bitmap {
        var src = source
        val factor = if (src.height <= src.width) src.height else src.width
        val longer = if (src.height >= src.width) src.height else src.width
        val x = if (src.height >= src.width) 0 else (longer - factor) / 2
        val y = if (src.height <= src.width) 0 else (longer - factor) / 2
        src = Bitmap.createBitmap(src, x, y, factor, factor)
        src = Bitmap.createScaledBitmap(src, scale, scale, false)
        return src
    }

    private fun checkPermissions(): Boolean {
        var camera = false
        var storage = false

        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            camera = true
        }
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            storage = true
        }


        if (!camera && !storage) {
            return true
        }

        val builder = AlertDialog.Builder(context)

        if (storage && camera) {
            val permissionsRequired = arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_camera_storage)

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE)
                && ActivityCompat.shouldShowRequestPermissionRationale(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {

                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        activity!!,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }

            } else {
                //Previously Permission Request was cancelled with Don't Ask Again',

                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context!!.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(
                        context!!,
                        "Go to Permissions and Allow Camera and Storage Permissions",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


        } else if (camera) {
            val permissionsRequired = arrayOf<String>(Manifest.permission.CAMERA)

            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_camera)

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.CAMERA
                )
            ) {
                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        activity!!,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }

            } else {
                //Previously Permission Request was cancelled with Don't Ask Again',

                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context!!.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(context!!, "Go to Permissions and Allow Camera Permission", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            val permissionsRequired =
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_storage)

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        activity!!,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }
            } else {
                //Previously Permission Request was cancelled with Don't Ask Again',
                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", context!!.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(
                        context!!,
                        "Go to Permissions and Allow Storage Permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()

        return false
    }

}
