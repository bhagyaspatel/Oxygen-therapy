package com.example.notification_trial02.Utils

sealed class NetworkResponse<T> (
    val data : T? = null,
    val error : String? = null
){
    class Success<T> (data: T? = null) : NetworkResponse<T>(data = data)
    class Failure<T> (error : String) : NetworkResponse<T> (error = error)
    class Loading<T> () : NetworkResponse<T>()
}