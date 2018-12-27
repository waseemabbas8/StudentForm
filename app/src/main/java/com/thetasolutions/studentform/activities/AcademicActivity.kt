package com.thetasolutions.studentform.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.CheckBox
import com.thetasolutions.studentform.R
import com.thetasolutions.studentform.helpers.AppConstants
import com.thetasolutions.studentform.helpers.PermissionsManager
import com.thetasolutions.studentform.helpers.toast
import com.thetasolutions.studentform.models.Student
import kotlinx.android.synthetic.main.activity_academic.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*




class AcademicActivity : AppCompatActivity() {

    private val RQ_CODE_CAMERA = 1
    private val RQ_CODE_GALLERY = 0
    var mCurrentPhotoPath: String?=null
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: PermissionsManager

    lateinit var student : Student
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic)

        student = intent.getSerializableExtra(AppConstants.KEY_STUDENT) as Student

        img_avatar.setOnClickListener {
            // Initialize a new instance of ManagePermissions class
            managePermissions = PermissionsManager(this,getListOfPermissions(),PermissionsRequestCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !managePermissions.allGranted)
                managePermissions.checkPermissions()

            if (managePermissions.allGranted){
                showImageDialog()
            }
        }

        cb_comp.setOnClickListener(CheckBoxEventListener())
        cb_math.setOnClickListener(CheckBoxEventListener())
        cb_eng.setOnClickListener(CheckBoxEventListener())

        btn_finish.setOnClickListener {
            goToProfile()
        }

    }

    // Receive the permissions request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            PermissionsRequestCode ->{
                val isPermissionsGranted = managePermissions
                    .processPermissionsResult(requestCode,permissions,grantResults)

                if(isPermissionsGranted){
                    // Do the task now
                    toast("Permissions granted.")
                }else{
                    toast("Permissions denied.")
                }
                return
            }
        }
    }

    private fun getListOfPermissions() : List<String> {
        // Initialize a list of required permissions to request runtime
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return list
    }

    private fun showImageDialog() {
        var builder = AlertDialog.Builder(this@AcademicActivity)
        builder.setTitle(AppConstants.IMG_DIALOG_TITLE)
        builder.setMessage(AppConstants.IMG_DIALOG_MESSAGE)
        builder.setIcon(R.drawable.ic_image_gray)
        builder.setPositiveButton(AppConstants.IMG_OPT_GALLERY){ dialog,which ->
            choosePhotoFromGallary()
        }
        builder.setNegativeButton(AppConstants.IMG_OPT_CAMERA){ dialog,which ->
            takePhotoFromCamera()
        }
        builder.create().show()
    }

    private fun goToProfile() {

        student.mobile = et_mobile.text?.toString()
        student.email = et_email.text?.toString()!!

        val profileIntent = Intent (this@AcademicActivity,ProfileActivity::class.java)
        profileIntent.putExtra(AppConstants.KEY_STUDENT,student)
        startActivity(profileIntent)

    }

    private fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, RQ_CODE_GALLERY)
    }

    private fun takePhotoFromCamera() {
//        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, RQ_CODE_CAMERA)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    toast("failed to save image")
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.thetasolutions.studentform",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, RQ_CODE_CAMERA)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode != Activity.RESULT_OK ) {
            return
        }
        when(resultCode){
            RQ_CODE_GALLERY ->{
                if (data != null){
                    val contentURI = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        img_avatar.setImageBitmap(bitmap)

                    } catch (e: IOException) {
                        e.printStackTrace()
                        toast("Error!")
                    }

                }
            }

            RQ_CODE_CAMERA ->{
                val imageBitmap = data?.extras?.get("data") as Bitmap
                img_avatar.setImageBitmap(imageBitmap)
            }
        }
    }

    // Method to save an image to internal storage

    inner class CheckBoxEventListener : View.OnClickListener{
        override fun onClick(view: View) {
            if (view is CheckBox){
                if (view.isChecked)
                   student.subjects.add(view.text.toString())
                else
                    student.subjects.remove(view.text.toString())
            }
        }

    }
}
