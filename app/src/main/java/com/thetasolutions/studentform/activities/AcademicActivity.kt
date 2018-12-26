package com.thetasolutions.studentform.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import com.thetasolutions.studentform.R
import com.thetasolutions.studentform.helpers.AppConstants
import com.thetasolutions.studentform.models.Student
import kotlinx.android.synthetic.main.activity_academic.*

class AcademicActivity : AppCompatActivity() {

    var student : Student = Student()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academic)

        cb_comp.setOnClickListener(CheckBoxEventListener())
        cb_math.setOnClickListener(CheckBoxEventListener())
        cb_eng.setOnClickListener(CheckBoxEventListener())

        btn_finish.setOnClickListener {
            goToProfile()
        }

        img_avatar.setOnClickListener {
            showImageDialog()
        }
    }

    private fun showImageDialog() {
        var builder = AlertDialog.Builder(this@AcademicActivity)
        builder.setTitle(AppConstants.IMG_DIALOG_TITLE)
        builder.setMessage(AppConstants.IMG_DIALOG_MESSAGE)
        builder.setPositiveButton(AppConstants.IMG_OPT_GALLERY){ dialog,which ->
            Toast.makeText(this@AcademicActivity, "GALLERY",Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(AppConstants.IMG_OPT_CAMERA){ dialog,which ->
            Toast.makeText(this@AcademicActivity, "Camera",Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    private fun goToProfile() {
        student = intent.getSerializableExtra(AppConstants.KEY_STUDENT) as Student

        student.mobile = et_mobile.text?.toString()
        student.email = et_email.text?.toString()!!

        val profileIntent = Intent (this@AcademicActivity,ProfileActivity::class.java)
        profileIntent.putExtra(AppConstants.KEY_STUDENT,student)
        startActivity(profileIntent)

    }

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
