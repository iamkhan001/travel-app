package com.nstudio.travelreminder.ui

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.nstudio.travelreminder.R
import com.nstudio.travelreminder.utils.OnActivityInteractionListener
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import com.nstudio.travelreminder.utils.AppDataProvider
import android.util.Log
import android.widget.Toast
import android.net.Uri.fromParts
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.content.Intent
import android.Manifest.permission






class MainActivity : AppCompatActivity(), OnActivityInteractionListener{

    companion object{

        private const val PERMISSION_CALLBACK_CONSTANT = 100
        private const val REQUEST_PERMISSION_SETTING = 101
        private val tag = MainActivity::class.java.simpleName
    }

    private val permissionsRequired = arrayOf(
        permission.CAMERA,
        READ_EXTERNAL_STORAGE,
        WRITE_EXTERNAL_STORAGE
    )

    private lateinit var dataProvider:AppDataProvider
    private lateinit var context:Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.nstudio.travelreminder.R.layout.activity_main)

        context = this@MainActivity
        dataProvider = AppDataProvider(context)

        checkPermissionsOld()

        if (savedInstanceState==null){
            val host = NavHostFragment.create(R.navigation.main_path)
            supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, host).setPrimaryNavigationFragment(host).commit()

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            var allGranted = false
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    allGranted = true
                } else {
                    allGranted = false
                    break
                }
            }

            if (allGranted) {
                Log.e(tag, "all permissions granted!")
            } else {
                Log.e(tag, "permissions denied")
            }

            dataProvider.dontAskPermission()
        }
    }

    private fun checkPermissions(): Boolean {
        var camera = false
        var storage = false

        if (ActivityCompat.checkSelfPermission(this, permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            camera = true
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            storage = true
        }


        if (!camera && !storage) {
            return true
        }

        val builder = AlertDialog.Builder(context)

        if (storage && camera) {
            val permissionsRequired = arrayOf(
                permission.CAMERA,
                WRITE_EXTERNAL_STORAGE,
                READ_EXTERNAL_STORAGE
            )

            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_camera_storage)

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.CAMERA)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
            ) {

                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }

            } else {
                //Previously Permission Request was cancelled with Don't Ask Again',

                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(
                        baseContext,
                        "Go to Permissions and Allow Camera and Storage Permissions",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }


        } else if (camera) {
            val permissionsRequired = arrayOf(permission.CAMERA)

            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_camera)

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    permission.CAMERA
                )
            ) {
                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }

            } else {
                //Previously Permission Request was cancelled with Don't Ask Again',

                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(baseContext, "Go to Permissions and Allow Camera Permission", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            val permissionsRequired =
                arrayOf(WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE)

            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_storage)

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    WRITE_EXTERNAL_STORAGE
                ) && ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    READ_EXTERNAL_STORAGE
                )
            ) {
                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    ActivityCompat.requestPermissions(
                        this,
                        permissionsRequired,
                        PERMISSION_CALLBACK_CONSTANT
                    )
                }
            } else {
                //Previously Permission Request was cancelled with Don't Ask Again',
                builder.setPositiveButton("Grant") { dialog, _ ->
                    dialog.cancel()
                    val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = fromParts("package", packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING)
                    Toast.makeText(baseContext, "Go to Permissions and Allow Storage Permission", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()

        return false
    }

    private fun checkPermissionsOld() {

        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
        ) {

            if (dataProvider.isPermissionAsked()) {
                return
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.permission_title)
            builder.setMessage(R.string.permission_message)
            builder.setPositiveButton("Grant") { dialog, _ ->
                dialog.cancel()
                ActivityCompat.requestPermissions(this@MainActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT)
            }
            builder.setNegativeButton(
                "Cancel"
            ) { dialog, _ -> dialog.cancel() }
            builder.show()
        }


    }


    override fun onFragmentBack() {
        onBackPressed()
    }

}
