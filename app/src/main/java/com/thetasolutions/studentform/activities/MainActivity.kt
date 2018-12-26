package com.thetasolutions.studentform.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.thetasolutions.studentform.R
import com.thetasolutions.studentform.helpers.AppConstants
import com.thetasolutions.studentform.models.Student
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_next.setOnClickListener {
            goToAcademicScreen()
        }
    }

    private fun goToAcademicScreen() {
        val student = Student()
        student.firstName = et_first_name.text?.toString()!!
        student.lastName = et_last_name.text?.toString()
        try {
            student.age = et_age.text.toString().toInt()
        }catch (e : Exception){
            student.age = 0
        }


        var selectedId = rg_gender.checkedRadioButtonId
        var gender : RadioButton = findViewById(selectedId)
        student.gender = gender.text.toString()

        val intent = Intent(this@MainActivity, AcademicActivity::class.java)
        intent.putExtra(AppConstants.KEY_STUDENT,student)
        startActivity(intent)
    }


}
