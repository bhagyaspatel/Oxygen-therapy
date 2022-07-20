package com.example.notification_trial02.modals

data class Prescription(
    val rsn: String ? = null,
    val system: String ? = null,
    val flowRate: String?= null,
    val humidification: String?= null,
    val saturation: Boolean? = null, //true for low, false for high
    val weaning: String? = null,
    val doctor: String? = null,
    val designation: String? = null,
    val kmc: String? = null,

    )