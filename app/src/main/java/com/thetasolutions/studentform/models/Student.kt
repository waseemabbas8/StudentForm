package com.thetasolutions.studentform.models

import java.io.Serializable

class Student : Serializable {
    var firstName : String = ""
    var lastName : String ? = null
    var age : Int = 0
    var gender : String ? = null
    var mobile : String ? = null
    var email : String = ""
    var avatar : String ? =null
    var subjects : ArrayList<String> = ArrayList()
}