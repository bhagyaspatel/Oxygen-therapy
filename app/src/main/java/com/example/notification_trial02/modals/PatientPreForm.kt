package com.example.notification_trial02.modals

enum class Period{
    Min15,
    Min30,
    HOURLY,
    HOURLY2,
    OTHER
}

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
            val system : String ? = null,
            val flowRateRecommended : String?= null,
            val flowRateGiven : String?= null,
            val monitoring : Period ?= null,
            val otherPeriod : String ?= null,
            val targetOxygenRec : String? = null,
            val oxygenTime : String? = null,
            val oxygenDate : String? = null,
            val TargetAchieveTime : String? = null,
            val mews : String? = null,
            var mewsRec : Boolean = true,
            val oxygenOffTime : String? = null,
            val oxygenOffDate : String? = null,
            val weaningTime : String? = null,
            val weaningDate : String? = null,
            val complication : String? = null
)