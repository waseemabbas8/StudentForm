package com.thetasolutions.studentform.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.thetasolutions.studentform.R
import com.thetasolutions.studentform.helpers.AppConstants
import com.thetasolutions.studentform.models.Student
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        showProfile()
    }

    private fun showProfile(){
        var student : Student = intent.getSerializableExtra(AppConstants.KEY_STUDENT) as Student
        tv_name.text = student.firstName + " " + student.lastName
        tv_age.text = student.age.toString() + " Years Old"
        tv_email.text = student.email
        tv_phone.text = student.mobile
        tv_gender.text = student.gender
        var subjectsList = "\n"
        for (subject in student.subjects){
            subjectsList += "- $subject\n"
        }

        tv_sub.text = subjectsList
    }
}
