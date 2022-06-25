package com.example.notification_trial02.modals

data class PatientPreForm (
            val rsn : String ? = null,
            val prescription : Boolean = true,
            val comorbidities : ArrayList<String> = arrayListOf(),
            val risk : Boolean = true,
            val saturation : Boolean = true ,
            val hr : String ? = null,
            val bp : String ? = null,
            val spo2 : String ? = null,
            val rr : String ? = null,
            val ph : String ? = null,
            val pco2 : String ? = null,
            val po2 : String ? = null,
            val hco3 : String ? = null,
            val lactate : String ? = null,
            val system : String ? = null
        )