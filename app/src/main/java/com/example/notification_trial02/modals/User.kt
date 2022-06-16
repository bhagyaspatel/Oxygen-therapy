package com.example.notification_trial02.modals

enum class UserType{
    ADMIN,
    CLIENT
}

data class User (
    val type : UserType? = null //"Admin" or "client"
)