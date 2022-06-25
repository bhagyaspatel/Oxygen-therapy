package com.example.notification_trial02.modals

import androidx.room.PrimaryKey

data class PatientAndHospital (
        val name : String? = null,
        val age : Int? = 1,
        val sex : String? = "male",
        val date : String? = null,
        val area : String? = null,
        val hospitalNumber : String? = null,
        val pk : String ? = null
        ){
        override fun equals(other: Any?): Boolean {

                if (javaClass != other?.javaClass){
                        return false
                }

                other as PatientAndHospital

                if (name != other.name)
                        return false
                if (age != other.age)
                        return false
                if (sex != other.sex)
                return false
                if (date != other.date)
                        return false
                if (area != other.area)
                        return false
                if (hospitalNumber != other.hospitalNumber)
                        return false

                return true
        }
}

